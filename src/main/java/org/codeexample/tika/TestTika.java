package org.codeexample.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class TestTika {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, org.xml.sax.SAXException, TikaException {
		String fileStr = "E:\\jeffery\\tmp\\1010\\Test Email with word attachmewnt.msg";
		fileStr = "E:\\jeffery\\doc\\tmp.txt";
		File file = new File(fileStr);
		InputStream is = new FileInputStream(file);
		Metadata metadata = new Metadata();
		ContentHandler handler = new BodyContentHandler();
		handler = new XHTMLContentHandler(handler, metadata);
		AutoDetectParser parser = new AutoDetectParser();
		String mimeType = new Tika().detect(file);
		metadata.set(Metadata.CONTENT_TYPE, mimeType);

		parser.parse(is, handler, metadata, new ParseContext());
		is.close();

		for (int i = 0; i < metadata.names().length; i++) {
			String item = metadata.names()[i];
			System.out.println(item + " -- " + metadata.get(item));
		}

		System.out.println(handler.toString());
	}
}