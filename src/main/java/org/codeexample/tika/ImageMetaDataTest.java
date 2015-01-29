package org.codeexample.tika;
import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ImageMetaDataTest {
	private static final String fileName = "IMG_2659.JPG";
	private Tika tika;
	private InputStream stream;

	@Before
	public void setUp() {
		tika = new Tika();
		stream = this.getClass().getResourceAsStream(fileName);
	}

	@Test
	public void testImageMetadataCameraModel() throws IOException,
			SAXException, TikaException {
		Metadata metadata = new Metadata();
		ContentHandler handler = new DefaultHandler();
		Parser parser = new JpegParser();
		ParseContext context = new ParseContext();
		String mimeType = tika.detect(stream);
		metadata.set(Metadata.CONTENT_TYPE, mimeType);
		parser.parse(stream, handler, metadata, context);

		assertTrue("The expected Model is not correct", metadata.get("Model")
				.equals("Canon EOS 350D DIGITAL"));
	}

	@After
	public void close() throws IOException {
		if (stream != null) {
			stream.close();
		}
	}
}