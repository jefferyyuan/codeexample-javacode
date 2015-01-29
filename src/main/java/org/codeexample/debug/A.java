package org.codeexample.debug;

import java.lang.instrument.Instrumentation;

class B {
	/* Header Info 8 Byte */
	int x; // 4
	float y; // 4
	long d; // 8
	String z; // 4
}

public class A {
	private class C {
	}

	/* new concept */
	public static void premain(String args, Instrumentation ins) {
		B obj = new B();
		long size = ins.getObjectSize(obj);
		System.out.println("B Size is " + size);
		size = ins.getObjectSize(new C());
		System.out.println("C size is " + size);
		String q = "test";
		size = ins.getObjectSize(q);
		System.out.println("Q String size is " + size);
	}

	public static void main(String[] args) {
		System.out.println("Inside Main ");
		premain(null, null);
	}
}