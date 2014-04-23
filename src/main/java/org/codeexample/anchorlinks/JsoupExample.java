package org.codeexample.anchorlinks;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.google.common.base.Stopwatch;

public class JsoupExample {

  // http://jsoup.org/cookbook/extracting-data/selector-syntax
  // Jsoup Selector doesn't like the ' or " around value
  private static final String TOC_ANCHOR = "div[id=toc] ul>li a[href^=#]:not([href=#])";
  private static final String PLAIN_ANCHOR_A_TAG = "a[href^=#]:not([href=#])";

  private static final int MAX_ANCHOR_LINKS = 5;
  // only <div id="bodyContent"> section
  private static final String PATTERN_BODY_ROOT = "div[id=bodyContent]",
      REGEX_EXTRACT_CONTENT = "<span.*id\\s*=\\s*(?:\"|')?{0}(?:'|\")?.*>(.*)</span>(.*)(<span.*id\\s*=\\s*(?:\"|')?{1}(?:'|\")?.*>.*</span>.*)";

  // REGEX_EXTRACT_CONTENT =
  // "<span[^>]*\\s*(?:\"|'')?{0}(?:''|\")?[^>]*>([^<]*)</span>(.*)(<span[^>]*\\s*(?:\"|'')?{1}(?:''|\")?[^>]*>[^<]*</span>.*)";;
  // 1000 times, avg: 11 or 12 mills
  // REGEX_EXTRACT_CONTENT =
  // "<span[^>]*id\\s*=\\s*(?:\"|'')?{0}(?:''|\")?[^>]*>([^<]*)</span>(.*?)(<span[^>]*(id\\s*=\\s?:\"|'')?{1}(?:''|\")?[^>]*>[^<]*</span>.*)";;

  // 100 times, avg: 24 mills
  // 1000 times, avg: 10 or 11 mills
  // REGEX_EXTRACT_CONTENT =
  // "<span[^>]*\\bid\\s*=\\s*(?:\"|'')?{0}(?:''|\")?[^>]*>([^<]*)</span>(.*?)<span[^>]*\\bid\\s*=\\s*(?:\"|'')?{1}(?:''|\")?[^>]*>[^<]*</span>";

  // , anchor1 = "JDK_contents", anchor2 = "Ambiguity_between_a_JDK_and_an_SDK";
  @Test
  public void testWiki() throws IOException {
    int times = 1;
    Stopwatch stopwatch = Stopwatch.createStarted();
    for (int i = 0; i < times; i++) {
      String url = "http://en.wikipedia.org/wiki/Java_Development_Kit";
      Map<String, String> anchorContents = parseAnchorContent(url);
      if (i == times - 1) {
        for (Entry<String, String> anchorContent : anchorContents.entrySet()) {
          System.out.println(anchorContent);
        }
      }
    }

    System.out.println("Avg took " + stopwatch.elapsed(TimeUnit.MILLISECONDS)
        / times);
    stopwatch.stop();
  }

  public Map<String, String> parseAnchorContent(String url) throws IOException {
    Map<String, String> anchorContents = new LinkedHashMap<String, String>();

    // Document doc = Jsoup.connect(url).get();
    Document doc = Jsoup
        .parse(
            new File(
                "E:/jeffery/code/github/codeexample-javacode/src/main/java/org/codeexample/anchorlinks/jdk-wiki.txt"),
            "UTF-8");
    Element rootElement = doc.select(PATTERN_BODY_ROOT).first();
    if (rootElement == null)
      return anchorContents;
    Set<String> anchors = getAnchors(rootElement);
    if (anchors.isEmpty())
      return anchorContents;
    StringBuilder remaining = new StringBuilder(rootElement.toString());

    Iterator<String> it = anchors.iterator();
    String current = it.next();
    while (it.hasNext() && remaining.length() > 0) {
      String next = it.next();
      anchorContents.put(current,
          getContentBetweenAnchor(remaining, current, next));
      // System.out.println(current + "=" + anchorContents.get(current));
      current = next;
    }
    // last one
    String lastTxt = Jsoup.parse(remaining.toString()).text();
    if (StringUtils.isNotBlank(lastTxt)) {
      anchorContents.put(current, lastTxt);
      // System.out.println(current + "=" + anchorContents.get(current));
    }
    return anchorContents;
  }

  public Set<String> getAnchors(Element rootElement) {
    Set<String> anchors = new LinkedHashSet<String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public boolean add(String e) {
        if (size() >= MAX_ANCHOR_LINKS)
          return false;
        return super.add(e);
      }
    };
    getAnchorsImpl(rootElement, TOC_ANCHOR, anchors);
    if (anchors.isEmpty()) {
      // no toc anchor found, then use
      getAnchorsImpl(rootElement, PLAIN_ANCHOR_A_TAG, anchors);
    }
    return anchors;
  }

  public void getAnchorsImpl(Element rootElement, String anchorPattern,
      Set<String> anchors) {
    Elements elements = rootElement.select(anchorPattern);
    if (!elements.isEmpty()) {
      for (Element element : elements) {
        String href = element.attr("href");
        anchors.add(href.substring(1));
      }
    }
  }

  public String getContentBetweenAnchor(StringBuilder remaining,
      String anchor1, String anchor2) throws IOException {

    // http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
    // "<a name='Creating_Schedule_Policies'>Creating Schedule Policies</a>XXXX<a name='Auxiliary_Copy_Schedule_Policy'>Creating Schedule Policies</a>";//
    // doc.toString();
    // long start = new Date().getTime();
    // <span class="mw-headline" id="JDK_contents">JDK contents</span><a
    // href="/wiki/AppletViewer" title="AppletViewer">appletviewer</a><span
    // class="mw-headline" id="Ambiguity_between_a_JDK_and_an_SDK">Ambiguity
    // between a JDK and an SDK</span>
    // <span[^>]*id\s*=\s*(?:"|')?JDK_contents(?:'|")?[^>]*>[^<]*</span>(.*)<span[^>]*?id\s*=\s*(?:"|')?Ambiguity_between_a_JDK_and_an_SDK(?:'|")?[^>]*>[^<]*</span>
    // StringBuilder sb = new StringBuilder();
    // // the first group is the anchor text
    // sb.append(matchAnchorRegexStr(anchor1, anchorElement, true))
    // // the second group is the text between these 2 anchors
    // .append("(.*)")
    // // the third group is the remaing text
    // .append("(").append(matchAnchorRegexStr(anchor2, anchorElement, false))
    // .append(".*)");

    String pattern = MessageFormat.format(REGEX_EXTRACT_CONTENT, anchor1,
        anchor2);
    // System.out.println(pattern);
    Matcher matcher = Pattern.compile(pattern,
        Pattern.DOTALL | Pattern.MULTILINE).matcher(remaining);
    String matchedText = "";
    if (matcher.find()) {
      // System.out.println("found match");
      String anchorText = Jsoup.parse(matcher.group(1)).text();
      matchedText = anchorText + " " + Jsoup.parse(matcher.group(2)).text();
      // System.out.println(matchedText);
      // int cnt = matcher.groupCount();
      // if (cnt == 2) {
      // int g2Start = matcher.start(2),;
      int g2End = matcher.end(2);
      remaining.delete(0, g2End);
      // System.out.println(remaining);
      // String newRemaining = matcher.group(3);
      // remaining.setLength(0);
      // remaining.append(newRemaining);
      // }
    }
    // long end = new Date().getTime();
    // System.out.println("Took: " + (end - start));
    return matchedText;
  }

  // public String matchAnchorRegexStr(String anchor1, String anchorElement,
  // boolean cpatureAnchorText) {
  // StringBuilder sb = new StringBuilder().append("<").append(anchorElement)
  // .append("[^>]*").append("\\s*").append("(?:\"|')?").append(anchor1)
  // .append("(?:'|\")?[^>]*>");
  // if (cpatureAnchorText) {
  // sb.append("([^<]*)");
  // } else {
  // sb.append("[^<]*");
  // }
  // return sb.append("</").append(anchorElement).append(">").toString();
  // }

  @Test
  public void testCode() throws IOException {
    String url = "http://en.wikipedia.org/wiki/Java_Development_Kit";
    Document doc = Jsoup.connect(url).get();
    Element rootElement = doc.select(PATTERN_BODY_ROOT).first();
    if (rootElement == null)
      return;
    Set<String> anchors = new LinkedHashSet<String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public boolean add(String e) {
        if (size() >= MAX_ANCHOR_LINKS)
          return false;
        System.out.println(e);
        return super.add(e);
      }
    };
    Elements elements = rootElement.select(TOC_ANCHOR);
    if (!elements.isEmpty()) {
      for (Element element : elements) {
        String href = element.attr("href");
        anchors.add(href.substring(1));
      }
    }

    System.out.println(anchors);
  }

  public void getContentBetweenAnchor2(String url, String anchor1,
      String anchor2) throws IOException {
    Document doc = Jsoup.connect(url).get();
    // "http://documentation.commvault.com/commvault/v10/article?p=features/schedule_policy/advanced.htm"
    // System.out.println(doc);a[name=Creating_Schedule_Policies]
    // span[id=JDK_contents]
    Element tag1 = doc.select("a[name=Creating_Schedule_Policies]").first();
    System.out.println(tag1);

    // http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
    String rawContent = doc.toString();
    // "<a name='Creating_Schedule_Policies'>Creating Schedule Policies</a>XXXX<a name='Auxiliary_Copy_Schedule_Policy'>Creating Schedule Policies</a>";//
    // doc.toString();
    long start = new Date().getTime();
    String pattern = "<a\\s*name\\s*=\\s*(?:'|\")?"
        + "Creating_Schedule_Policies" + "(?:'|\")?.*>.*</a>" + "(.*)"
        + "<a\\s*name\\s*=\\s*(?:'|\")" + "Auxiliary_Copy_Schedule_Policy"
        + "(?:'|\").*>.*</a>";
    // pattern =
    // "<a\\s*name\\s*=\\s*(?:\"|')?Creating_Schedule_Policies(?:'|\")?[^>]*>[^<]*</a>(.*)<a\\s*name\\s*=\\s*(?:\"|')Auxiliary_Copy_Schedule_Policy(?:'|\")[^>]*>[^<]*</a>";
    System.out.println(pattern);
    Matcher matcher = Pattern.compile(pattern,
        Pattern.DOTALL | Pattern.MULTILINE).matcher(rawContent);
    if (matcher.find()) {
      System.out.println("found match");
      String matchedHtml = matcher.group(1);
      System.out.println(matchedHtml);
      System.out.println("*************************");

      // String matchedText = Jsoup.parse(matchedHtml).text();
      // System.out.println(matchedText);
    }
    long end = new Date().getTime();

    System.out.println("Took: " + (end - start));
  }

}
