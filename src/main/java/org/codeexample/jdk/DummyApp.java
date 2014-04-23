package org.codeexample.jdk;
import java.util.ArrayList;


public class DummyApp {


/**

	 * @param args

	 */

	public static void main(String[] args) throws Exception {

		ArrayList anyList = new ArrayList();

		while (true) {

			Thread.sleep(100);

			anyList.add(random());

		}

	}


public static String random() {

		StringBuffer s = new StringBuffer();

		int i = 0;

		while (i < 400) {

			s.append((char) (int) (Math.random() * 10 + 65));

			i++;

		}

		return s.toString();

	}


}
