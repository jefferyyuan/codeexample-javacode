package org.codeexample.jdk8;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * * Java program to demonstrate how you can use lambda expression in place of *
 * Anonymous Inner class to implement event handling logic in Swing application.
 * * Any listener code is lot concise and more readable using lambda expression.
 */
public class Test extends JFrame {
	private final JButton button = new JButton("Start");

	public Test() {
		super("Java 8 Lambda Example");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				System.out
						.println("Classic way of handling event using Anonymous class");
			}
		});
		button.addActionListener(e -> System.out.println("Java 8 way"
				+ " to handle event using Lambda expression"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
	}

	public static void main(String[] args) {
		// Lambda expressiont to implement Runnable interface
		SwingUtilities.invokeLater(() -> {
			new Test().setVisible(true);
		});
	}
}
