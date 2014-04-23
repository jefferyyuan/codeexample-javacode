package org.codeexample.jdk;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * From http://www.rgagnon.com/javadetails/java-0651.html
 * 
 */
public class SystemUtils {

  private SystemUtils() {
  }

  public static long getPID() {
    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    String processName = java.lang.management.ManagementFactory
        .getRuntimeMXBean().getName();
    return Long.parseLong(processName.split("@")[0]);
  }

  public static void main(String[] args) {
    String msg = "My PID is " + SystemUtils.getPID();

    javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, msg,
        "SystemUtils", javax.swing.JOptionPane.DEFAULT_OPTION);

  }

}