package org.codeexample.jdk;

import java.lang.management.*;
import java.util.*;

/**
 * From http://www.informit.com/guides/content.aspx?g=java&seqNum=249
 * 
 */
public class Java5ManagementTest {
  public static void dumpMemoryInfo() {
    try {
      System.out.println("\nDUMPING MEMORY INFO\n");
      // Read MemoryMXBean
      MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
      System.out.println("Heap Memory Usage: "
          + memorymbean.getHeapMemoryUsage());
      System.out.println("Non-Heap Memory Usage: "
          + memorymbean.getNonHeapMemoryUsage());

      // Read Garbage Collection information
      List<GarbageCollectorMXBean> gcmbeans = ManagementFactory
          .getGarbageCollectorMXBeans();
      for (GarbageCollectorMXBean gcmbean : gcmbeans) {
        System.out.println("\nName: " + gcmbean.getName());
        System.out.println("Collection count: " + gcmbean.getCollectionCount());
        System.out.println("Collection time: " + gcmbean.getCollectionTime());
        System.out.println("Memory Pools: ");
        String[] memoryPoolNames = gcmbean.getMemoryPoolNames();
        for (int i = 0; i < memoryPoolNames.length; i++) {
          System.out.println("\t" + memoryPoolNames[i]);
        }
      }

      // Read Memory Pool Information
      System.out.println("Memory Pools Info");
      List<MemoryPoolMXBean> mempoolsmbeans = ManagementFactory
          .getMemoryPoolMXBeans();
      for (MemoryPoolMXBean mempoolmbean : mempoolsmbeans) {
        System.out.println("\nName: " + mempoolmbean.getName());
        System.out.println("Usage: " + mempoolmbean.getUsage());
        System.out.println("Collection Usage: "
            + mempoolmbean.getCollectionUsage());
        System.out.println("Peak Usage: " + mempoolmbean.getPeakUsage());
        System.out.println("Type: " + mempoolmbean.getType());
        System.out.println("Memory Manager Names: ");
        String[] memManagerNames = mempoolmbean.getMemoryManagerNames();
        for (int i = 0; i < memManagerNames.length; i++) {
          System.out.println("\t" + memManagerNames[i]);
        }
        System.out.println("\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    dumpMemoryInfo();
    // Tweak memory a little bit
    for (int i = 0; i < 1000000; i++) {
      String s = "My String " + i;
    }
    dumpMemoryInfo();
  }
}