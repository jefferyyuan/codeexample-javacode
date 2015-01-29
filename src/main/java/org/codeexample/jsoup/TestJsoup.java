package org.codeexample.jsoup;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class TestJsoup {
	public static void main(String[] args) throws MalformedURLException,
			IOException {
		// Document doc = Jsoup
		// .parse(
		// new URL(
		// "http://abcnews.go.com/Health/wireStory/investigators-branson-spacecraft-crash-site-26619288"),
		// 1000 * 60);
		String old = "<textarea id=\"embed\"><br/><a href=\"http://abcnews.go.com/us\">More ABC US news</a> | <a href=\"http://abcnews.go.com/health\">ABC Health News</a></textarea>";

		Document doc = Jsoup.parse(old);
		doc.select("a").remove();

		Element el = doc.body();

		Elements divs = el.select("textarea");

		for (Element tmp : divs) {
			String html = tmp.html();
			if (!html.endsWith(".")) {
				html += "11111111111111";
			}

			tmp.html(html);
		}

		Whitelist whitelist = Whitelist.simpleText();
		String result = Jsoup.clean(doc.html(), whitelist);

		System.out.println(result);
	}
}
