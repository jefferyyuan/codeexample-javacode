package org.codeexample.jdk;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Test;

import com.sun.management.HotSpotDiagnosticMXBean;

public class TestManagementFactory {

  public static void main(String[] args) {
    // containsThread("");
    getPID();
  }

  private void checkMemoryUsuage() {
    long max = Runtime.getRuntime().maxMemory() / (1024 * 1024);
    long allocated = Runtime.getRuntime().totalMemory() / (1024 * 1024);
    long free = Runtime.getRuntime().freeMemory() / (1024 * 1024);
    long consumed = allocated - free;
    long available = free + (max - allocated);
    // ThreadMXBean bean = ManagementFactory.getThreadMXBean();
    // System.out
    // .println(
    // "MEMORY USAGE: max [%s], allocated [%s], free [%s], consumed [%s], available [%s], threads [%s]",
    // max, allocated, free, consumed, available, bean.getThreadCount());

    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    try {
      HotSpotDiagnosticMXBean bean = ManagementFactory.newPlatformMXBeanProxy(
          server, "com.sun.management:type=HotSpotDiagnostic",
          HotSpotDiagnosticMXBean.class);
      bean.setVMOption("HeapDumpOnOutOfMemoryError", "true");
    } catch (Throwable th) {
      th.printStackTrace();
    }

    // add the security manager to jvm
    // System.setSecurityManager(new WorkflowSecurityManager());
  }

  private void findDeadLocks() {
    ThreadMXBean bean = ManagementFactory.getThreadMXBean();
    long[] threadIds = bean.findDeadlockedThreads();
    if (threadIds != null) {
      synchronized (this) {
        ThreadInfo[] infos = bean.getThreadInfo(threadIds);
        for (ThreadInfo info : infos) {
          System.out.println("!!!! DEADLOCK DETECTED !!!! "
              + info.toString().trim());
        }
      }
    }
  }

  public void getRemoteMBean() throws IOException, InterruptedException {
    JMXServiceURL jmxUrl = new JMXServiceURL(
        "service:jmx:rmi:///jndi/rmi://remoteServer:9999/jmxrmi");
    JMXConnector jmxConn = JMXConnectorFactory.connect(jmxUrl);
    MBeanServerConnection mbsc = jmxConn.getMBeanServerConnection();
    MemoryMXBean mbean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
        ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);

    while (true) {
      System.out.println(mbean.getHeapMemoryUsage());
      System.out.println(mbean.getNonHeapMemoryUsage());
      Thread.sleep(100);
    }
  }

  public void generateHeapDump(String fileName, boolean live)
      throws IOException {
    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    HotSpotDiagnosticMXBean bean = ManagementFactory.newPlatformMXBeanProxy(
        server, "com.sun.management:type=HotSpotDiagnostic",
        HotSpotDiagnosticMXBean.class);
    bean.dumpHeap(fileName, live);
  }

  public static boolean isThreadRunning(String threadName) {
    boolean rhreadRunning = false;
    ThreadMXBean threadMX = ManagementFactory.getThreadMXBean();
    long[] tids = threadMX.getAllThreadIds();
    ThreadInfo[] tinfos = threadMX.getThreadInfo(tids, Integer.MAX_VALUE);
    for (ThreadInfo ti : tinfos) {
      String tName = ti.getThreadName();
      if (tName.startsWith(threadName)) {
        rhreadRunning = true;
      }
    }
    return rhreadRunning;
  }

  /**
   * From http://www.rgagnon.com/javadetails/java-0651.html
   */
  public static long getPID() {
    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    String processName = runtimeMXBean.getName();
    return Long.parseLong(processName.split("@")[0]);
  }

  public void testManagementFactory() {
    MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
    List<MemoryPoolMXBean> mempoolsmbeans = ManagementFactory
        .getMemoryPoolMXBeans();
    List<GarbageCollectorMXBean> gcmbeans = ManagementFactory
        .getGarbageCollectorMXBeans();
  }

  @Test
  public void testDetectDeadLock() {
    final Object lock1 = new Object();
    final Object lock2 = new Object();

    Thread thread1 = new Thread(new Runnable() {
      @Override
      public void run() {
        synchronized (lock1) {
          System.out.println("Thread1 acquired lock1");
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException ignore) {
          }
          synchronized (lock2) {
            System.out.println("Thread1 acquired lock2");
          }
        }
      }

    });
    thread1.start();

    Thread thread2 = new Thread(new Runnable() {
      @Override
      public void run() {
        synchronized (lock2) {
          System.out.println("Thread2 acquired lock2");
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException ignore) {
          }
          synchronized (lock1) {
            System.out.println("Thread2 acquired lock1");
          }
        }
      }
    });
    thread2.start();

    // Wait a little for threads to deadlock.
    try {
      TimeUnit.MILLISECONDS.sleep(100);
    } catch (InterruptedException ignore) {
    }
    detectDeadlock();
    // Wait a little for threads to deadlock.
    try {
      TimeUnit.MILLISECONDS.sleep(100);
    } catch (InterruptedException ignore) {
    }
  }

  @Test
  public void test2() {
    final ReentrantLock lock1 = new ReentrantLock();
    final ReentrantLock lock2 = new ReentrantLock();

    Thread thread1 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          lock1.lock();
          System.out.println("Thread1 acquired lock1");
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException ignore) {
          }
          lock2.lock();
          System.out.println("Thread1 acquired lock2");
        } finally {
          lock2.unlock();
          lock1.unlock();
        }
      }
    });
    thread1.start();

    Thread thread2 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          lock2.lock();
          System.out.println("Thread2 acquired lock2");
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException ignore) {
          }
          lock1.lock();
          System.out.println("Thread2 acquired lock1");
        } finally {
          lock1.unlock();
          lock2.unlock();
        }
      }
    });
    thread2.start();

    // Wait a little for threads to deadlock.
    try {
      TimeUnit.MILLISECONDS.sleep(100);
    } catch (InterruptedException ignore) {
    }

    detectDeadlock();
    // Wait a little for threads to deadlock.
    try {
      TimeUnit.MILLISECONDS.sleep(100);
    } catch (InterruptedException ignore) {
    }
  }

  public long[] findDeadlockedThreads() {
    // JDK 1.5 only supports the findMonitorDeadlockedThreads()
    // method, so you need to comment out the following three lines
    ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    if (threadBean.isSynchronizerUsageSupported())
      return threadBean.findDeadlockedThreads();
    else
      return threadBean.findMonitorDeadlockedThreads();
  }

  public static void detectDeadlock() {
    ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    long[] threadIds = null;
    threadIds = threadBean.findDeadlockedThreads();
    // if (threadBean.isSynchronizerUsageSupported()) {
    // threadIds = threadBean.findDeadlockedThreads();
    // } else {
    // threadIds= threadBean.findMonitorDeadlockedThreads();
    // }
    int deadlockedThreads = threadIds != null ? threadIds.length : 0;
    System.out.println("Number of deadlocked threads: " + deadlockedThreads);

    if (threadIds != null) {
      ThreadInfo[] infos = threadBean.getThreadInfo(threadIds);
      for (ThreadInfo info : infos) {
        System.out.println("hanged thread: " + info);
      }
    }
  }
}
