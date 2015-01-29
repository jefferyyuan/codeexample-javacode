package org.codeexample.debug;

import java.util.Arrays;

public class C {
	private int[] x = new int[100000000];
	private int y;

	public C() {
		Arrays.fill(x, 1);
	}

	public static void main(String[] args) {
		System.out.println(ObjectSizeFetcher.getObjectSize(new C()));
	}
}