package org.codeexample.url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class TestUrlParts {

	
	public static void main(String[] args) throws URISyntaxException, MalformedURLException {
		URL url = new URL("http://172.19.96.210:8080/commvault/v10/article?p=products/search/compliance/tag_set.htm");
		System.out.println(url.getPath());

		System.out.println(url.getQuery());
//		URL newurl = new URL("", url.get)
	}
}
