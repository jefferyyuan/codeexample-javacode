package org.codeexample.overflow;

import com.google.common.math.LongMath;

/**
 * @author administrator
 * 
 */
public class MathOverflow {

  public static void main(String[] args) {

    LongMath.checkedAdd(Long.MAX_VALUE, 1);
    LongMath.checkedAdd(Long.MIN_VALUE, -1);

    System.out.println(safeMultiply(Integer.MIN_VALUE, 2));
    System.out.println(safeMultiply(Integer.MAX_VALUE, -2));

    System.out.println(safeAdd(Integer.MAX_VALUE, 1));
    System.out.println(safeAdd(-1, Integer.MIN_VALUE));

  }

  public static long intRangeCheck(long value) throws ArithmeticException {
    if ((value < Integer.MIN_VALUE) || (value > Integer.MAX_VALUE)) {
      throw new ArithmeticException("Integer overflow");
    }
    return value;
  }

  public static int multAccum(int oldAcc, int newVal, int scale)
      throws ArithmeticException {
    final long res = intRangeCheck(((long) oldAcc)
        + intRangeCheck((long) newVal * (long) scale));
    return (int) res; // safe down-cast
  }

  /**
   * From
   * https://www.securecoding.cert.org/confluence/display/java/NUM00-J.+Detect
   * +or+prevent+integer+overflow
   */
  static final int safeAdd(int left, int right) throws ArithmeticException {
    if (right > 0 ? left > Integer.MAX_VALUE - right : left < Integer.MIN_VALUE
        - right) {
      throw new ArithmeticException("Integer overflow");
    }
    return left + right;
  }

  static final int safeSubtract(int left, int right) throws ArithmeticException {
    if (right > 0 ? left < Integer.MIN_VALUE + right : left > Integer.MAX_VALUE
        + right) {
      throw new ArithmeticException("Integer overflow");
    }
    return left - right;
  }

  static final int safeMultiply(int left, int right) throws ArithmeticException {
    if (right > 0 ? left > Integer.MAX_VALUE / right
        || left < Integer.MIN_VALUE / right
        : (right < -1 ? left > Integer.MIN_VALUE / right
            || left < Integer.MAX_VALUE / right : right == -1
            && left == Integer.MIN_VALUE)) {
      throw new ArithmeticException("Integer overflow");
    }
    return left * right;
  }

  static final int safeDivide(int left, int right) throws ArithmeticException {
    if ((left == Integer.MIN_VALUE) && (right == -1)) {
      throw new ArithmeticException("Integer overflow");
    }
    return left / right;
  }

  static final int safeNegate(int a) throws ArithmeticException {
    if (a == Integer.MIN_VALUE) {
      throw new ArithmeticException("Integer overflow");
    }
    return -a;
  }

  static final int safeAbs(int a) throws ArithmeticException {
    if (a == Integer.MIN_VALUE) {
      throw new ArithmeticException("Integer overflow");
    }
    return Math.abs(a);
  }
}
