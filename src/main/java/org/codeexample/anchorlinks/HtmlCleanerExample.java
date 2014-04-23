package org.codeexample.anchorlinks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.junit.Test;

public class HtmlCleanerExample {
  @Test
  public void test() throws MalformedURLException, IOException,
      XPatherException {
    Set<String> anchors = getTOCAnchor("http://documentation.commvault.com/commvault/v10/article?p=features/schedule_policy/advanced.htm");

    anchors = getTOCAnchor("http://documentation.commvault.com/commvault/v10/article?p=getting_started/storage.htm");
  }

  public void getContent() throws MalformedURLException, IOException {
    HtmlCleaner cleaner = new HtmlCleaner();
    TagNode node = cleaner
        .clean(new URL(
            "http://documentation.commvault.com/commvault/v10/article?p=features/schedule_policy/advanced.htm"));

  }

  public Set<String> getTOCAnchor(String url) throws MalformedURLException,
      IOException, XPatherException {
    Set<String> anchors = new LinkedHashSet<String>();
    HtmlCleaner cleaner = new HtmlCleaner();
    TagNode node = cleaner.clean(new URL(url));
    // Object[] myNodes = node
    // .evaluateXPath("//div[@class='toc_content']/ul/li//a/@href");

    Object[] myNodes = node
        .evaluateXPath("//a[@name='Creating_Schedule_Policies']/following-sibling::str | //a[@name='Auxiliary_Copy_Schedule_Policy']");

    if (myNodes != null && myNodes.length > 0) {
      for (Object obj : myNodes) {
        String str = (String) obj;
        if (str.startsWith("#")) {
          anchors.add(str);
          System.out.println(str);
        }
      }
    } else {
      myNodes = node.evaluateXPath("//a/@href");
      // for (Object obj : myNodes) {
      // String str = (String) obj;
      // if (str.startsWith("#")) {
      // anchors.add(str);
      // System.out.println(str);
      // }
      // }
    }

    return anchors;
  }
}
