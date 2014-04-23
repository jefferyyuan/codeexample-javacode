package org.codeexample.btrace;

import com.sun.btrace.AnyType;
import com.sun.btrace.aggregation.Aggregation;
import com.sun.btrace.aggregation.AggregationFunction;
import com.sun.btrace.annotations.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class SocketMonitor {

  private static Map<Object, Socket> streamMap = newWeakMap();

  /**
   * A helper aggregation instance summing Data-In and Data-Out per socket
   */
  private static Aggregation socketDataSum = newAggregation(AggregationFunction.SUM);

  @Property
  /**
   * dataIn will get published as a JMX property of the btrace/SocketMonitor bean
   */
  private static AtomicLong dataIn = newAtomicLong(0L);
  @Property
  /**
   * dataOut will get published as a JMX property of the btrace/SocketMonitor bean
   */
  private static AtomicLong dataOut = newAtomicLong(0L);

  @TLS
  /**
   * A thread safe helper variable to keep the instance of a Socket
   * that getInput/OutputStream method is applied to
   */
  private static Socket currentSocket;
  @TLS
  /**
   * A thread safe helper variable to keep the instance of an InputStream
   * that read method is applied to
   */
  private static InputStream currentInputStream;

  /**
   * Intercept the entry to the getInput/OutputStream method call Store the
   * Socket instance in currentSocket variable
   */
  @OnMethod(clazz = "+java.net.Socket", method = "/get(Input|Output)Stream/", location = @Location(Kind.ENTRY))
  /* BTrace 1.0: public static void onGetStreamEntry(Socket self) */
  public static void onGetStreamEntry(@Self Socket self) {
    currentSocket = self;
  }

  /**
   * Intercept the normal exit of the getInputStream method call Upon the exit
   * the instance of the created InputStream is known so we can bind it with the
   * Socket instance used to obtain the stream
   */
  @OnMethod(clazz = "+java.net.Socket", method = "getInputStream", location = @Location(Kind.RETURN))
  /* BTrace 1.0: public static void onInputStream(InputStream stream) */
  public static void onInputStream(@Return InputStream stream) {
    put(streamMap, stream, currentSocket);
    currentSocket = null;
  }

  /**
   * Intercept the normal exit of the getOutputStream method call Upon the exit
   * the instance of the created OutputStream is known so we can bind it with
   * the Socket instance used to obtain the stream
   */
  @OnMethod(clazz = "+java.net.Socket", method = "getOutputStream", location = @Location(Kind.RETURN))
  /* BTrace 1.0: public static void onOutputStream(OutputStream stream) */
  public static void onOutputStream(@Return OutputStream stream) {
    put(streamMap, stream, currentSocket);
    currentSocket = null;
  }

  /**
   * Store the InputStream instance used in the read method call
   */
  @OnMethod(clazz = "+java.io.InputStream", method = "read", location = @Location(Kind.ENTRY))
  /*
   * BTrace 1.0: public static void onRead(AnyType[] args) { InputStream self =
   * (InputStream) args[0];
   */
  public static void onRead(@Self InputStream self, AnyType[] args) {
    if (containsKey(streamMap, self)) {
      currentInputStream = self;
    } else {
      currentInputStream = null;
    }
  }

  /**
   * Use the stored InputStream instance to get hold of the defining Socket
   * instance. Then use the byte count available as the result of the method
   * call to update the aggregation and total values
   */
  @OnMethod(clazz = "+java.io.InputStream", method = "read", location = @Location(Kind.RETURN))
  /* BTrace 1.0: public static void countReadData(int count) */
  public static void countReadData(@Return int count) {
    if (count > -1 && currentInputStream != null) {
      Socket sck = get(streamMap, currentInputStream);
      addAndGet(dataIn, count);
      addToAggregation(socketDataSum, newAggregationKey(str(sck), "Input"),
          count);
      currentInputStream = null;
    }
  }

  /**
   * The following three methods intercept and process three different forms of
   * the write method call. The separation is necessary to be able to get hold
   * of strongly typed parameters wchich we can use in oreder to extract
   * valuable information
   */

  @OnMethod(clazz = "+java.io.OutputStream", method = "write", location = @Location(Kind.ENTRY))
  /* BTrace 1.0: public static void onWrite(Object self, int byteValue) */
  public static void onWrite(@Self Object self, int byteValue) {
    if (containsKey(streamMap, self)) {
      Socket sck = (Socket) get(streamMap, self);
      addAndGet(dataOut, 1L);
      addToAggregation(socketDataSum, newAggregationKey(str(sck), "Output"), 1);
    }
  }

  @OnMethod(clazz = "+java.io.OutputStream", method = "write", location = @Location(Kind.ENTRY))
  /* BTrace 1.0: public static void onWrite(Object self, byte[] data) */
  public static void onWrite(@Self Object self, byte[] data) {
    if (containsKey(streamMap, self)) {
      Socket sck = (Socket) get(streamMap, self);
      addAndGet(dataOut, data.length);
      addToAggregation(socketDataSum, newAggregationKey(str(sck), "Output"),
          data.length);
    }
  }

  @OnMethod(clazz = "+java.io.OutputStream", method = "write", location = @Location(Kind.ENTRY))
  /*
   * BTrace 1.0: public static void onWrite(Object self, byte[] data, int
   * offset, int length)
   */
  public static void onWrite(@Self Object self, byte[] data, int offset,
      int length) {
    if (containsKey(streamMap, self)) {
      Socket sck = (Socket) get(streamMap, self);
      addAndGet(dataOut, length);
      addToAggregation(socketDataSum, newAggregationKey(str(sck), "Output"),
          length);
    }
  }

  /**
   * BTrace event handler - when the BTrace engine receives this event it will
   * dump the aggregation data to the client
   */
  @OnEvent("dump_stats")
  public static void dumpData() {
    printAggregation("Summary of the data read by socket", socketDataSum);
  }

  /**
   * BTrace event handler - when received the aggregation data as well as the
   * totals are reset
   */
  @OnEvent("clear_stats")
  public static void reset() {
    println("Resetting collected data...");
    clearAggregation(socketDataSum);
    set(dataIn, 0L);
    set(dataOut, 0L);
    println("Data reset");
  }
}