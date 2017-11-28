package com.demo.dao;

public class PojoFacadeController implements BusinessActionController {
	private static Logger log = LoggerFactory
			.getLogger(PojoFacadeController.class);
	private static BusinessAction action = BusinessActionFactory
			.getBusinessAction("com.bdcc.waf.controller.business.action.ServiceWrapper");

	private static boolean getRollbackOnly(Event event) {
		return event.get("waf.business.controller.rollbackOnly") != null;
	}

	public ActionResult executeCommand(Event event) {
		ActionResult actionResult = createActionResult();
		return executeCommand(event, false, actionResult);
	}

	private static ActionResult executeCommand(Event event, boolean isGroup,
			ActionResult actionResult) {
		if (actionResult == null) {
			actionResult = createActionResult();
		}

		UnrecoverableException myException = null;
		boolean isRollback = false;

		try {
			if (getRollbackOnly(event)) {
				isRollback = true;
			}

			BusinessFilterFactory e = BusinessFilterFactory.getInstance();
			BusinessFilterChain eo = e.getBusinessFilterChain(action);
			eo.doFilter(event, actionResult);
			HibernateFactory.checkAndFlushSession();

			try {
				if (actionResult.getStatus() < 0
						|| ThreadLocalResourceManager.getRollbackOnly()) {
					isRollback = true;
				}
			} catch (Exception arg8) {
				;
			}

			if (!isGroup && !isRollback) {
				ThreadLocalResourceManager.closeAllResource();
			}
		} catch (UnrecoverableException arg9) {
			myException = arg9;
		} catch (RuntimeException arg10) {
			myException = new UnrecoverableException(
					"Unpredictable system exception at runtime", arg10);
		} catch (Exception arg11) {
			if (arg11.getCause() != null) {
				myException = new UnrecoverableException(arg11.getCause()
						.getMessage(), arg11);
			} else {
				myException = new UnrecoverableException("User Exception",
						arg11);
			}
		} catch (Throwable arg12) {
			myException = new UnrecoverableException("System Fatal Exception",
					arg12);
		}

		if (myException != null) {
			if ("User Define Exception".equals(myException
					.getUserFriendlyErrorMessage())) {
				Object[] e1 = new Object[]{myException.getOriginalThrowable()
						.getMessage()};
				actionResult.addError("User Define Exception", e1);
			} else {
				ErrorObject e2 = myException.export();
				actionResult.addErrorObject(e2);
				log.error(
						"[" + e2.getMessageId() + "] "
								+ e2.getOriginalMessage(),
						myException.getOriginalThrowable());
			}
		}

		try {
			if (myException != null || isRollback) {
				ThreadLocalResourceManager.rollbackAllResource();
			}
		} catch (UnrecoverableException arg7) {
			ErrorObject eo1 = arg7.export();
			actionResult.addErrorObject(eo1);
		}

		return actionResult;
	}

	private static ActionResult createActionResult() {
		BaseActionResult result = new BaseActionResult();
		return result;
	}
}