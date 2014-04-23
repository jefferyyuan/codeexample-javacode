package org.codeexample.format;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class LocaleFormatUsage {
  // private static Locale locale = new Locale("zh", "cn");
  Locale[] locales = { Locale.CHINA, Locale.CHINESE, Locale.ITALY,
      Locale.ITALIAN, Locale.JAPAN, Locale.JAPANESE };

  @Test
  public void testDefaultEncoding() throws UnsupportedEncodingException {
    System.out.println(System.getProperty("file.encoding"));
    PrintStream sysout = new PrintStream(System.out, true, "UTF-8");
    // print 大家好
    sysout.println("\u5927\u5bb6\u597d");
  }

  @Test
  public void numberFormat() throws ParseException {
    for (Locale locale : locales) {
      NumberFormat nf = NumberFormat.getNumberInstance(locale);
      System.out.println(nf.format(10.011));
      System.out.println(nf.parse("10.01").doubleValue());

      nf = NumberFormat.getCurrencyInstance(locale);
      String value = nf.format(10.01);
      System.out.println(value);
      System.out.println(nf.parse(value));
    }

    Locale locale = new Locale("fi", "FI");
    NumberFormat nf = NumberFormat.getNumberInstance(locale);
    System.out.println(nf.format(10.011));
    System.out.println(nf.parse("10.01").doubleValue());
  }

  @Test
  public void messageFormat() throws ParseException {
    for (Locale locale : locales) {
      MessageFormat mf = new MessageFormat("{0,number,currency}", locale);
      System.out.println(mf.format(new Object[] { 10.011 }));
    }
  }

  @Test
  public void simpleDateFormat() throws ParseException {
    for (Locale locale : locales) {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale);
      String value = df.format(new Date());
      System.out.println(value);
      System.out.println(df.parse(value));
    }
  }
}
