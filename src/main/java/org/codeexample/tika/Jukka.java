package org.codeexample.tika;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ParserDecorator;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Jukka {
	public static void main(String[] args) throws Exception {
		Parser parser = new RecursiveMetadataParser(new AutoDetectParser());
		String file = "E:\\jeffery\\tmp\\1010\\Test Email with word attachmewnt.msg";
		file = "E:\\jeffery\\doc\\doc.zip";

		parser = new RecursiveTrackingMetadataParser(new AutoDetectParser(),
				file);
		ParseContext context = new ParseContext();
		context.set(Parser.class, parser);

		ContentHandler handler = new DefaultHandler();
		Metadata metadata = new Metadata();

		InputStream stream = TikaInputStream
				.get(new File(file));
		try {
			parser.parse(stream, handler, metadata, context);
		} finally {
			stream.close();
		}
	}

	private static class RecursiveTrackingMetadataParser extends
			ParserDecorator {
		private String location;
		private int unknownCount = 0;

		public RecursiveTrackingMetadataParser(Parser parser, String location) {
			super(parser);
			this.location = location;
			if (!this.location.endsWith("/")) {
				this.location += "/";
			}
		}

		@Override
		public void parse(InputStream stream, ContentHandler ignore,
				Metadata metadata, ParseContext context) throws IOException,
				SAXException, TikaException {
			// Work out what this thing is
			String objectName = null;
			if (metadata.get(TikaMetadataKeys.RESOURCE_NAME_KEY) != null) {
				objectName = metadata.get(TikaMetadataKeys.RESOURCE_NAME_KEY);
			} else if (metadata.get(TikaMetadataKeys.EMBEDDED_RELATIONSHIP_ID) != null) {
				objectName = metadata
						.get(TikaMetadataKeys.EMBEDDED_RELATIONSHIP_ID);
			} else {
				objectName = "embedded-" + (++unknownCount);
			}
			String objectLocation = this.location + objectName;

			// Fetch the contents, and recurse if possible
			ContentHandler content = new BodyContentHandler();
			Parser preContextParser = context.get(Parser.class);
			context.set(Parser.class, new RecursiveTrackingMetadataParser(
					getWrappedParser(), objectLocation));
			super.parse(stream, content, metadata, context);
			context.set(Parser.class, preContextParser);

			// Report what this one is
			System.out.println("----");
			System.out.println("Resource is " + objectLocation);
			System.out.println("----");
			System.out.println(metadata);
			System.out.println("----");
			System.out.println(content.toString());
		}
	}

	private static class RecursiveMetadataParser extends ParserDecorator {

		public RecursiveMetadataParser(Parser parser) {
			super(parser);
		}

		@Override
		public void parse(InputStream stream, ContentHandler ignore,
				Metadata metadata, ParseContext context) throws IOException,
				SAXException, TikaException {
			ContentHandler content = new BodyContentHandler();
			super.parse(stream, content, metadata, context);

			System.out.println("----");
			System.out.println(metadata);
			System.out.println("----");
			System.out.println(content.toString());
		}
	}
}
