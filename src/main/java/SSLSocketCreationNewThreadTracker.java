import static com.sun.btrace.BTraceUtils.incrementAndGet;
import static com.sun.btrace.BTraceUtils.newAtomicLong;
import static com.sun.btrace.BTraceUtils.println;
import static com.sun.btrace.BTraceUtils.str;
import static com.sun.btrace.BTraceUtils.strcat;
import static com.sun.btrace.BTraceUtils.timestamp;

import java.util.concurrent.atomic.AtomicLong;

import com.sun.btrace.BTraceUtils.Threads;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.Self;

@BTrace(unsafe = true)
public class SSLSocketCreationNewThreadTracker {
  // @OnMethod(clazz = "/sun\\..*/", method = "/.*/")
  // public static void onsunImplInit() {
  // println("=====================");
  //
  // Threads.jstack();
  // }
  // @Property
  private static AtomicLong startThreadCount = newAtomicLong(0);

  // @Property
  private static AtomicLong sslSocketCount = newAtomicLong(0);

  // private static PoolingClientConnectionManager connectionManager;

  // @OnMethod(clazz = "com.sun.net.ssl.internal.ssl.SSLSocketImpl", method =
  // "<init>")

  // @OnMethod(clazz = "/.*\\.SSLSocketImpl/", method = "<init>")
  // public static void onSSLSocketImpl() {
  // println("==========SSLSocketImpl===========");
  // // Threads.jstack();
  // incrementAndGet(sslSocketCount);
  // println(timestamp());
  // println(strcat("Created ssl sockets: ", str(sslSocketCount)));
  //
  // }

  // @OnMethod(clazz =
  // "org.apache.http.impl.conn.PoolingClientConnectionManager", method =
  // "<init>")
  // public static void onNewPoolingClientConnectionManager(
  // @Self PoolingClientConnectionManager param) {
  // println(strcat(timestamp(),
  // "===========onNewPoolingClientConnectionManager=========="));
  // connectionManager = param;
  // }

  @OnMethod(clazz = "java.lang.Thread", method = "start")
  public static void onStartThread(@Self Thread t) {
    // updating counter is easy. Just assign to
    // the static field!
    // println("==========onnewThread===========");
    // Threads.jstack();
    long count = incrementAndGet(startThreadCount);
    println(strcat(timestamp(), strcat(", started threads: ", str(count))));
    // println(strcat("Started threads: ", str(startThreadCount)));
  }

  @OnMethod(clazz = "java.lang.Thread", method = "<init>")
  public static void onInitThread(@Self Thread t) {
    // updating counter is easy. Just assign to
    // the static field!
    println(strcat(timestamp(), "===========onInitThread=========="));
    println(timestamp());
    Threads.jstack();
  }

  @OnMethod(clazz = "/.*\\.SSLSocketFactory/", method = "createSocket")
  public static void onHttpClientConnectSocket() {
    println(strcat(timestamp(), "===========createSocket=========="));
    println(timestamp());
    Threads.jstack();
    long count = incrementAndGet(sslSocketCount);
    println(strcat(timestamp(), strcat(", created ssl sockets: ", str(count))));
  }

  // @OnTimer(6000 * 2)
  // public static void ontimer() {
  // // we can access counter as "count" as well
  // // as from jvmstat counter directly.
  // PoolStats stats = connectionManager.getTotalStats();
  // println(strcat(timestamp(), stats.toString()));
  // }

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
