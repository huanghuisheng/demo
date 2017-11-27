package com.demo.dao;

import org.omg.CORBA.CompletionStatus;

public abstract class SystemException1 extends RuntimeException {
	public int minor;
	public CompletionStatus completed;

	protected SystemException1(String arg0, int arg1, CompletionStatus arg2) {
		super(arg0);
		this.minor = arg1;
		this.completed = arg2;
	}

	public String toString() {
		String arg0 = super.toString();
		int arg1 = this.minor & -4096;
		switch (arg1) {
		case 1330446336:
			arg0 = arg0 + "  vmcid: OMG";
			break;
		case 1398079488:
			arg0 = arg0 + "  vmcid: SUN";
			break;
		default:
			arg0 = arg0 + "  vmcid: 0x" + Integer.toHexString(arg1);
		}

		int arg2 = this.minor & 4095;
		arg0 = arg0 + "  minor code: " + arg2;
		switch (this.completed.value()) {
		case 0:
			arg0 = arg0 + "  completed: Yes";
			break;
		case 1:
			arg0 = arg0 + "  completed: No";
			break;
		case 2:
		default:
			arg0 = arg0 + " completed: Maybe";
		}

		return arg0;
	}
}
