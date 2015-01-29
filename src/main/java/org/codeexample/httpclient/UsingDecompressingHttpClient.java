package org.codeexample.httpclient;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class UsingDecompressingHttpClient {
	private static final String URL_STRING = "http://rt.com/news/201435-woman-google-street-breasts/";

	@Test
	public void usingDefualtHttpClient() throws Exception {
		// output would be garbled characters in http client 4.2.
		HttpClient httpClient = new DefaultHttpClient();
		getContent(httpClient, new URI(URL_STRING));
	}

	@Test
	public void usingDecompressingHttpClient() throws Exception {
		// use DecompressingHttpClient to handle gzip response in  http client 4.2.
		HttpClient httpCLient = new DecompressingHttpClient(
				new DefaultHttpClient());
		getContent(httpCLient, new URI(URL_STRING));
	}

	private void getContent(HttpClient httpClient, URI url) throws IOException,
			ClientProtocolException {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpRsp = httpClient.execute(httpGet);
		String text = EntityUtils.toString(httpRsp.getEntity());

		for (Header header : httpRsp.getAllHeaders()) {
			System.out.println(header);
		}
		System.out.println(text);
	}

	@Test
	public void usingHttpClientBuilderIn43() throws Exception {
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = builder.build();
		getContent(httpClient, new URI(URL_STRING));
	}
}
