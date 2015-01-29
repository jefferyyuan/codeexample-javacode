package org.codeexample.hash;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class GuavaHash {

	private static QueueASCComparator Q_ASC_CMP = new QueueASCComparator();
	private static QueueDESCComparator Q_DESC_CMP = new QueueDESCComparator();

	static class QueueASCComparator<E extends Comparable<E>> implements
			Comparator<E> {
		@Override
		public int compare(E o1, E o2) {
			return o1.compareTo(o2);
		}
	}

	static class QueueDESCComparator<E extends Comparable<E>> implements
			Comparator<E> {
		@Override
		public int compare(E o1, E o2) {
			return o2.compareTo(o1);
		}
	}

	static class BoundedPriorityQueue<E extends Comparable<E>> extends
			PriorityQueue<E> {
		private static final long serialVersionUID = 1L;
		private long maxSize;
		private boolean maxN;

		@SuppressWarnings("unchecked")
		public BoundedPriorityQueue(int maxSize, boolean maxN) {
			// if maxN is true, then PriorityQueue keeps max n elements, the
			// first element in the queue is the min element, if current >= min,
			// then add it, otherwise skip it. <br>
			// if maxN is false, the PriorityQueue keeps min n elements, the
			// first element in the queue is the max element, if current <= max,
			// then add it, otherwise skip it. <br>

			super(maxSize, maxN ? Q_ASC_CMP : Q_DESC_CMP);
			this.maxSize = maxSize;
			this.maxN = maxN;
		}

		// private BoundedPriorityQueue(int maxSize,
		// Comparator<? super E> comparator) {
		// super(maxSize, comparator);
		// this.maxSize = maxSize;
		// }

		@Override
		public boolean add(E e) {
			if (this.size() >= maxSize) {

				if (maxN) {
					// compare with element 0
					if (e.compareTo(this.peek()) >= 0) {
						E removed = this.poll();
						// System.out.println("removed " + removed);
						return super.add(e);
					}
				} else {
					if (e.compareTo(this.peek()) <= 0) {
						E removed = this.poll();
						// System.out.println("removed " + removed);
						return super.add(e);
					}
				}
				return false;
			} else {
				return super.add(e);
			}
		}
	}

	@Test
	public void testBoudedPriorityQueue() {
		// get max 10
		BoundedPriorityQueue<Integer> bouned = new BoundedPriorityQueue<Integer>(
				10, true);

		List<Integer> list = new ArrayList<Integer>();
		addTestData(100, list);
		addTestData(100, list);

		bouned.addAll(list);

		System.out.println(bouned);

		// get min 10
		bouned = new BoundedPriorityQueue<Integer>(10, true);
		list = new ArrayList<Integer>();
		addTestData(100, list);
		bouned.addAll(list);
		//
		System.out.println(bouned);
	}

	@Test
	public void testMinBoudedPriorityQueue() {
		// get max 10
		BoundedPriorityQueue<Integer> bouned = new BoundedPriorityQueue<Integer>(
				10, false);

		List<Integer> list = new ArrayList<Integer>();
		addTestData(100, list);
		addTestData(100, list);

		bouned.addAll(list);

		System.out.println(bouned);

		printOrderdQueue(bouned);
		// get min 10
		bouned = new BoundedPriorityQueue<Integer>(10, false);
		list = new ArrayList<Integer>();
		addTestData(100, list);
		bouned.addAll(list);
		//
		System.out.println(bouned);
		printOrderdQueue(bouned);

		MinMaxPriorityQueue<Integer> minMax = MinMaxPriorityQueue.create();
	}

	private void printOrderdQueue(BoundedPriorityQueue<Integer> bouned) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		while (bouned.size() > 0) {
			list.add(bouned.remove());
		}

		Iterator<Integer> it = list.descendingIterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	private void addTestData(int size, List<Integer> list) {
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		Collections.shuffle(list);
	}

	public static void main(String[] args) {

		Pattern pattern = Pattern
				.compile("http://docs.commvault.com/commvault/*");
		boolean b = pattern
				.matcher(
						"http://docs.commvault.com/commvault/css/cv-home-bootstrap.min.css?_0115201412")
				.matches();

		System.out.println(b);
		//
		// String str =
		// "This is a build break notification regarding CVContentAnalyzer for v11 B44. Please review and take action as needed.";
		// HashFunction hf = Hashing.sipHash24();
		//
		// HashCode hc = hf.newHasher().putString(str, Charset.forName("utf-8"))
		// .hash();
		// System.out.println(hc.toString().length());
		// System.out.println(hc.asLong());
		//
		// hc = hf.newHasher().putString(str, Charset.forName("utf-8")).hash();
		// System.out.println(hc.toString());
		// System.out.println(hc.asLong());
	}

}
