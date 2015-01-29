package org.codeexample.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.EmbeddedContentHandler;
import org.junit.Test;

public class TikaOutlookParser {

	@Test
	public void testOutlookParser() throws Exception {
		File file = new File(
				"E:\\jeffery\\tmp\\1010\\Test Email with word attachmewnt.msg");
		InputStream is = new FileInputStream(file);
		Metadata metadata = new Metadata();
		BodyContentHandler bodyHandler = new BodyContentHandler();
		EmbeddedContentHandler embeddedContentHandler = new EmbeddedContentHandler(bodyHandler);

		AutoDetectParser parser = new AutoDetectParser();

		String mimeType = new Tika().detect(file);
		metadata.set(Metadata.CONTENT_TYPE, mimeType);

		parser.parse(is, embeddedContentHandler, metadata, new ParseContext());
		is.close();

		System.out.println("Metadaa: ");
		for (int i = 0; i < metadata.names().length; i++) {
			String item = metadata.names()[i];
			System.out.println(item + " -- " + metadata.get(item));
		}
		System.out.println("Body: ");
		System.out.println(embeddedContentHandler.toString());

	}

}
