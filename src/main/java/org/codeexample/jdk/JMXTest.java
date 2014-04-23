package org.codeexample.jdk;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXTest {

  /**
   * 
   * @param args
   */

  public static void main(String[] args) throws Exception {

    JMXServiceURL jmxUrl = new JMXServiceURL(
        "service:jmx:rmi:///jndi/rmi://localhost:7777/jmxrmi");
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

}