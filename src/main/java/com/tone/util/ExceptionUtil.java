package com.tone.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtil {
	public static String getStackTrace(Throwable throwable) {
		if (throwable == null) {
			return null;
		} else {
			StringWriter out = new StringWriter();
			PrintWriter writer = new PrintWriter(out);
			throwable.printStackTrace(writer);
			out.flush();
			return out.toString();
		}
	}

	public static String getExceptionID() {
		String id = "" + System.currentTimeMillis();
		return id;
	}

	private static String formatMessage(String messageKey, Object[] parameter) {
		StringBuffer sb = new StringBuffer();
		char[] exp = messageKey.toCharArray();
		int pos = 0;
		int maxLen = exp.length;

		while (true) {
			while (pos < maxLen) {
				int len;
				String num;
				int paramNumber;
				Object parameter1;
				if (exp[pos] == 37) {
					len = getPos(exp, pos, '%');
					if (len > 0) {
						num = new String(exp, pos + 1, len);
						paramNumber = Integer.parseInt(num.trim());
						parameter1 = getParameter(parameter, paramNumber);
						if (parameter1 == null) {
							parameter1 = "%?%";
						}

						sb.append(parameter1);
						pos = pos + len + 2;
						continue;
					}
				}

				if (exp[pos] == 123) {
					len = getPos(exp, pos, '}');
					if (len > 0) {
						num = new String(exp, pos + 1, len);
						paramNumber = Integer.parseInt(num.trim());
						parameter1 = getParameter(parameter, paramNumber);
						if (parameter1 == null) {
							parameter1 = "%?%";
						}

						sb.append(parameter1);
						pos = pos + len + 2;
						continue;
					}
				}

				sb.append(exp[pos]);
				++pos;
			}

			return sb.toString();
		}
	}

	private static int getPos(char[] charArray, int begin, char match) {
		int maxLen = charArray.length - begin;

		for (int i = 1; i < maxLen && i < 20; ++i) {
			if (charArray[begin + i] == match) {
				return i - 1;
			}
		}

		return 0;
	}

	private static Object getParameter(Object[] parameter, int num) {
		return parameter != null && parameter.length != 0
				? (num <= parameter.length && num != 0
						? parameter[num - 1]
						: null) : null;
	}

	public static void main(String[] argc) throws Exception {
		String message = "abc{0}=%0%=%{0}{1}";
		Object[] para = new Object[]{"111"};
		String result = formatMessage(message, para);
		System.out.println(result);
	}
}
