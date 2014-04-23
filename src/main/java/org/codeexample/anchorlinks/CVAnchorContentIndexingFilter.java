/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codeexample.anchorlinks;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * Indexing filter that offers an option to either index all inbound anchor text
 * for a document or deduplicate anchors. Deduplication does have it's con's,
 * 
 */
public class CVAnchorContentIndexingFilter {

  // test page:
  // http://internaldocs.commvault.com:8080/commvault/v10/article?p=features/schedule_policy/advanced.htm

  private static final String DEFAULT_REGEX_TOC_ANCHOR = "div[class=toc_content]>ul>li a[href^=#]:not([href=#]";
  // private static final String TOC_ANCHOR =
  // "div[id=toc] ul>li a[href^=#]:not([href=#])";
  private static final String DEFAULT_REGEX_PLAIN_ANCHOR_TAG = "a[href^=#]:not([href=#])";

  private static final int DEFAULT_MAX_ANCHOR_LINKS = 20;
  // only <div id="bodyContent"> section
  // private static final String PATTERN_BODY_ROOT = "div[id=bodyContent]";
  private static final String DEFAULT_REGEX_BODY_ROOT = "div[id=bodyContent]",
      // <span[^>]*\s*(?:"|')?JDK_contents(?:'|")?[^>]*>([^<]*)</span>(.*)(<span[^>]*\s*(?:"|')?Ambiguity_between_a_JDK_and_an_SDK(?:'|")?[^>]*>[^<]*</span>.*)
      DEFAULT_REGEX_EXTRACT_CONTENT = "<span[^>]*\\s*(?:\"|'')?{0}(?:''|\")?[^>]*>([^<]*)</span>(.*)(<span[^>]*\\s*(?:\"|'')?{1}(?:''|\")?[^>]*>[^<]*</span>.*)";

  private String regexTocAnchor = DEFAULT_REGEX_TOC_ANCHOR,
  // if can't find tocAnchor in web page, revert to plainAnchorTag
      regexPlainAnchorTag = DEFAULT_REGEX_PLAIN_ANCHOR_TAG,
      // if exists, only search content in this section
      regexBodyRoot = DEFAULT_REGEX_BODY_ROOT;
  /**
   * the regex to extract content between two tags: <br>
   * 1. The string must have 2 place holder {0}, {1}, it will be replaced by the
   * anchor name at runtime.<br>
   * 2. There must be 3 regex group, the first group is to extract the text in
   * the first anchor, the second group is to extract content between the two
   * anchors, the third is to extract the remaing text including the second
   * anchor.<br>
   * 3. If ther is single quote ' in the regex string, have to replaced by
   * doubled single quotes '' due to the usage of MessageFormat.check:
   * http://docs.oracle.com/javase/7/docs/api/java/text/MessageFormat.html <br>
   * One exampe:
   * <a[^>]*\\s*(?:\"|'')?{0}(?:''|\")?[^>]*>([^<]*)</a>(.*)(<a[^>]*\
   * \s*(?:\"|'')?{1}(?:''|\")?[^>]*>[^<]*</a>.*)
   */
  private String regexExtractContent = DEFAULT_REGEX_EXTRACT_CONTENT;

  private int maxAnachorLinks = DEFAULT_MAX_ANCHOR_LINKS;

  @Test
  public void test() throws IOException {
    String url = "http://internaldocs.commvault.com:8080/commvault/v10/article?p=features/schedule_policy/advanced.htm";
    Document rootDoc = Jsoup.connect(url).get();
    try {
      Map<String, String> anchors = parseAnchors(rootDoc);
      for (Entry<String, String> anchor : anchors.entrySet()) {
        if (StringUtils.isNotBlank(anchor.getKey())
            && StringUtils.isNotBlank(anchor.getValue())) {
        }
      }
    } catch (IOException e) {
    }
  }

  public Map<String, String> parseAnchors(Document rootDoc) throws IOException {
    Map<String, String> anchorContents = new LinkedHashMap<String, String>();
    Element rootElement = rootDoc;
    if (regexBodyRoot != null) {
      rootElement = rootDoc.select(regexBodyRoot).first();
    }
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
          getContentBetweenAnchorInWiki(remaining, current, next));
      current = next;
    }
    // last one
    String lastTxt = Jsoup.parse(remaining.toString()).text();
    if (StringUtils.isNotBlank(lastTxt)) {
      anchorContents.put(current, lastTxt);
    }
    return anchorContents;
  }

  public Set<String> getAnchors(Element rootElement) {
    Set<String> anchors = new LinkedHashSet<String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public boolean add(String e) {
        if (size() >= maxAnachorLinks)
          return false;
        return super.add(e);
      }
    };
    getAnchorsImpl(rootElement, regexTocAnchor, anchors);
    if (anchors.isEmpty()) {
      // no toc anchor found, then use
      getAnchorsImpl(rootElement, regexPlainAnchorTag, anchors);
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

  public String getContentBetweenAnchorInWiki(StringBuilder remaining,
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
    Matcher matcher = Pattern.compile(
        getRegexToExtractContent(anchor1, anchor2),
        Pattern.DOTALL | Pattern.MULTILINE).matcher(remaining);
    String matchedText = "";
    if (matcher.find()) {
      // System.out.println("found match");
      String anchorText = Jsoup.parse(matcher.group(1)).text();
      matchedText = anchorText + " " + Jsoup.parse(matcher.group(2)).text();
      // System.out.println(matchedText);
      // int cnt = matcher.groupCount();
      // if (cnt == 2) {
      String newRemaining = matcher.group(3);
      remaining.setLength(0);
      remaining.append(newRemaining);
      // }
    }
    // long end = new Date().getTime();
    // System.out.println("Took: " + (end - start));
    return matchedText;
  }

  private String getRegexToExtractContent(String anchor1, String anchor2) {
    return MessageFormat.format(regexExtractContent, anchor1, anchor2);
  }

  // public String getRegexToExtractContent(String anchor1, String anchor2,
  // String anchorElement) {
  // StringBuilder sb = new StringBuilder();
  // // the first group is the anchor text
  // sb.append(matchAnchorRegexStr(anchor1, anchorElement, true))
  // // the second group is the text between these 2 anchors
  // .append("(.*)")
  // // the third group is the remaing text
  // .append("(").append(matchAnchorRegexStr(anchor2, anchorElement, false))
  // .append(".*)");
  // return sb.toString();
  // }

}
