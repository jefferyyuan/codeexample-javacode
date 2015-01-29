package org.codeexample.jdk8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {

	private static void readStreamOfLinesUsingFilesWithTryBlock()
			throws IOException {
		Path path = Paths.get("c:/temp", "data.txt");
		// When filteredLines is closed, it closes underlying stream as well as
		// underlying file.
		try (Stream<String> filteredLines = Files.lines(path)
				// test if file is closed or not
				.onClose(() -> System.out.println("File closed"))
				.filter(s -> s.contains("password"))) {
			Optional<String> hasPassword = filteredLines.findFirst();
			if (hasPassword.isPresent()) {
				System.out.println(hasPassword.get());
			}
		}
	}

	public static void main(String args[]) {

		// testGenerator();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++) {
			list.add(i);
		}
		Stream<Integer> stream = list.stream();
		stream.forEach(p -> System.out.println(p));
		stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		stream.forEach(p -> System.out.println(p));

		stream = list.stream();
		List<Integer> evenNumbersList = stream.filter(i -> i % 2 == 0).collect(
				Collectors.toList());
		System.out.print(evenNumbersList);

		stream = list.stream();
		Integer[] evenNumbersArr = stream.filter(i -> i % 2 == 0).toArray(
				Integer[]::new);
		System.out.print(evenNumbersArr);

		List<String> memberNames = new ArrayList<>();
		memberNames.add("Amitabh");
		memberNames.add("Shekhar");
		memberNames.add("Aman");
		memberNames.add("Rahul");
		memberNames.add("Shahrukh");
		memberNames.add("Salman");
		memberNames.add("Yana");
		memberNames.add("Lokesh");

		boolean matched = memberNames.stream().anyMatch(
				(s) -> s.startsWith("A"));

		System.out.println(matched);

		String firstMatchedName = memberNames.stream()
				.filter((s) -> s.startsWith("L")).findFirst().get();

		System.out.println(firstMatchedName);

		stream = list.parallelStream();
		evenNumbersArr = stream.filter(i -> i % 2 == 0).toArray(Integer[]::new);
		System.out.print(evenNumbersArr);
		//
		// // Initialization of Collection
		//
		// List<Order> orderBook = new ArrayList<>();
		// Order buyGoogle = new Order("GOOG.NS", 300, 900.30, Order.Side.BUY);
		// Order sellGoogle = new Order("GOOG.NS", 600, 890.30,
		// Order.Side.SELL);
		// Order buyApple = new Order("APPL.NS", 400, 552, Order.Side.BUY);
		// Order sellApple = new Order("APPL.NS", 200, 550, Order.Side.SELL);
		// Order buyGS = new Order("GS.NS", 300, 130, Order.Side.BUY);
		// orderBook.add(buyGoogle);
		// orderBook.add(sellGoogle);
		// orderBook.add(buyApple);
		// orderBook.add(sellApple);
		// orderBook.add(buyGS);
		//
		// // Java 8 Streams Example 1 : Filtering Collection elements
		// // Filtering buy and sell order using filter() method of
		// // java.util.Stream class
		//
		// Stream<Order> stream = orderBook.stream();
		// Stream buyOrders = stream.filter((Order o) -> o.side().equals(
		// Order.Side.BUY));
		// System.out.println("No of Buy Order Placed :" + buyOrders.count());
		// Stream<Order> sellOrders = orderBook.stream().filter(
		// (Order o) -> o.side() == Order.Side.SELL);
		// System.out.println("No of Sell Order Placed : " +
		// sellOrders.count());
		//
		// // Java 8 Streams Example 2 : Reduce or Fold operation
		// // Calculating total value of all orders
		//
		// double value = orderBook.stream().mapToDouble((Order o) -> o.price())
		// .sum();
		// System.out.println("Total value of all orders : " + value);
		// long quantity = orderBook.stream().mapToLong((Order o) ->
		// o.quantity())
		// .sum();
		// System.out.println("Total quantity of all orders : " + quantity);
	}

	private static void testGenerator() {
		Stream<Date> stream = Stream.generate(() -> {
			return new Date();
		});
		stream.forEach(p -> System.out.println(p));
	}
}

class Order {
	enum Side {
		BUY, SELL;
	}

	private final String symbol;
	private final int quantity;
	private double price;
	private final Side side;

	public Order(String symbol, int quantity, double price, Side side) {
		this.symbol = symbol;
		this.quantity = quantity;
		this.price = price;
		this.side = side;
	}

	public double price() {
		return price;
	}

	public void price(double price) {
		this.price = price;
	}

	public String symbol() {
		return symbol;
	}

	public int quantity() {
		return quantity;
	}

	public Side side() {
		return side;
	}
}
