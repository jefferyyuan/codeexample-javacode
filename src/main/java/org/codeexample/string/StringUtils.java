package org.codeexample.string;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.google.common.base.Stopwatch;

public class StringUtils {

  private final static Pattern LTRIM = Pattern.compile("^\\s+");
  private final static Pattern RTRIM = Pattern.compile("\\s+$");

  public static void main(String[] args) {

    int times = 10000000;
    Stopwatch stopwatch = Stopwatch.createStarted();
    for (int i = 0; i < times; i++) {
      String str = "  hello world  ";
      ltrim(str);
    }
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS)
        + " for ltrim(using regular expression)");

    stopwatch.reset();
    stopwatch.start();
    for (int i = 0; i < times; i++) {
      String str = "  hello world  ";
      ltrim3(str);
    }
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS)
        + " for ltrim3(the straightforward one)");
    stopwatch.stop();
  }

  public static String ltrim(String s) {
    return LTRIM.matcher(s).replaceAll("");
  }

  public static String rtrim(String s) {
    return RTRIM.matcher(s).replaceAll("");
  }

  public static String ltrim3(String s) {
    int i = 0;
    while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
      i++;
    }
    return s.substring(i);
  }

  public static String rtrim3(String s) {
    int i = s.length() - 1;
    while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
      i--;
    }
    return s.substring(0, i + 1);
  }

}
