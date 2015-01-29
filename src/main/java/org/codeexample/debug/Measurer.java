package org.codeexample.debug;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import objectexplorer.ObjectGraphMeasurer;
import objectexplorer.ObjectGraphMeasurer.Footprint;

public class Measurer {

	public static void main(String[] args) {
		Set<Long> hashset = new HashSet<Long>();
		Random rng = new Random();
		int n = 10000;
		for (int i = 1; i <= n; i++) {
			hashset.add(rng.nextLong());
		}

		Footprint ft = ObjectGraphMeasurer.measure(hashset);
		System.out.println(ft);
	}
}