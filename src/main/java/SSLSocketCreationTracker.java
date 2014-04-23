import static com.sun.btrace.BTraceUtils.println;

import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

import java.util.concurrent.atomic.AtomicLong;

import com.sun.btrace.BTraceUtils.Threads;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.OnTimer;
import com.sun.btrace.annotations.Property;

@BTrace
public class SSLSocketCreationTracker {
  // @OnMethod(clazz = "/sun\\..*/", method = "/.*/")
  // public static void onsunImplInit() {
  // println("=====================");
  //
  // Threads.jstack();
  // }

  @Property
  private static AtomicLong count = newAtomicLong(0);

  @OnMethod(clazz = "com.sun.net.ssl.internal.ssl.SSLSocketImpl", method = "<init>")
  public static void onSSLSocketImpl() {
    // println("==========SSLSocketImpl===========");
    // Threads.jstack();
    // count.incrementAndGet();
    incrementAndGet(count);
  }

  @OnTimer(2000)
  public static void ontimer() {
    println(strcat("Created socket: ", str(count)));
  }

  // @OnMethod(clazz = "org.apache.http.conn.ssl.SSLSocketFactory", method =
  // "connectSocket")
  // public static void onHttpClientConnectSocket() {
  // println("=====================");
  // Threads.jstack();
  // }
  //
  // @OnMethod(clazz =
  // "org.apache.http.impl.conn.DefaultClientConnectionOperator", method =
  // "openConnection")
  // public static void on2() {
  // println("=====================");
  // Threads.jstack();
  // }

  // @OnMethod(clazz = "/.*\\.sun\\.security\\..*/", method = "/.*/")
  // public static void onSecurityInit() {
  // println("=====================");
  // Threads.jstack();
  // }
  //
  // @OnMethod(clazz = "/.*\\.sun\\.security\\.ssl.*/", method = "/.*/")
  // public static void onSSLSocketImplInit() {
  // println("=====================");
  // Threads.jstack();
  // }
  //
  // @OnMethod(clazz = "/.*\\.sun\\.security\\.ssl.*/", method = "/.*/")
  // public static void onSSLInit() {
  // println("=====================");
  // Threads.jstack();
  // }

  // @OnMethod(clazz =
  // "/org\\.apache\\.http\\.conn\\.ssl\\.SSLSocketFactory.*/", method = "/.*/")
  // public static void onhttpClient() {
  // println("=====================");
  // Threads.jstack();
  // }

  // @OnMethod(clazz = "/.*\\.SolrMain2/", method = "/.*/")
  // public static void onHello() {
  // println("=====================");
  // Threads.jstack();
  // }
}
