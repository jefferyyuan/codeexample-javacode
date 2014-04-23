import static com.sun.btrace.BTraceUtils.incrementAndGet;
import static com.sun.btrace.BTraceUtils.newAtomicLong;
import static com.sun.btrace.BTraceUtils.println;
import static com.sun.btrace.BTraceUtils.str;
import static com.sun.btrace.BTraceUtils.strcat;

import java.util.concurrent.atomic.AtomicLong;

import com.sun.btrace.BTraceUtils.Threads;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.OnTimer;
import com.sun.btrace.annotations.Property;
import com.sun.btrace.annotations.Self;

/**
 * This sample creates a jvmstat counter and increments it everytime
 * Thread.start() is called. This thread count may be accessed from outside the
 * process. The @Export annotated fields are mapped to jvmstat counters. The
 * counter name is "btrace." + <className> + "." + <fieldName>
 */
@BTrace
public class ThreadCounter {

  // create a jvmstat counter using @Export
  @Property
  private static AtomicLong count = newAtomicLong(0);

  @OnMethod(clazz = "java.lang.Thread", method = "start")
  public static void onnewThread(@Self Thread t) {
    // updating counter is easy. Just assign to
    // the static field!
    println("=====================");
    Threads.jstack();
    incrementAndGet(count);
  }

  @OnMethod(clazz = "java.lang.Thread", method = "<init>")
  public static void onInitThread(@Self Thread t) {
    // updating counter is easy. Just assign to
    // the static field!
    println("=====================");
    Threads.jstack();
  }

  @OnTimer(2000)
  public static void ontimer() {
    // we can access counter as "count" as well
    // as from jvmstat counter directly.
    println(strcat("Started threads: ", str(count)));
    // // or equivalently ...
    // println(Counters
    // .perfLong("btrace.com.sun.btrace.samples.ThreadCounter.count"));
  }
}