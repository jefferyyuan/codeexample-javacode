package org.codeexample.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

public class HandleEmbeddedParser extends AbstractParser {

	public static void main(String[] args) throws Exception {
		final AutoDetectParser parser = new AutoDetectParser();
		RecursiveMetadataParser p = new RecursiveMetadataParser(
				new AutoDetectParser(), false);

		InputStream input = new FileInputStream(new File(
				"E:\\jeffery\\tmp\\1010\\Test Email with word attachmewnt.msg"));
		Metadata metadata = new Metadata();
		ParseContext context = new ParseContext();
		context.set(Parser.class, new HandleEmbeddedParser(parser));
		BodyContentHandler handler = new BodyContentHandler();
		parser.parse(input, handler, metadata, context);

		System.out.println("Metadaa: ");
		for (int i = 0; i < metadata.names().length; i++) {
			String item = metadata.names()[i];
			System.out.println(item + " -- " + metadata.get(item));
		}
		System.out.println("Body: ");
		System.out.println(handler.toString());
	}

	private static final long serialVersionUID = 1L;
	public List<File> found = new ArrayList<File>();
	private AutoDetectParser parser;

	public HandleEmbeddedParser(AutoDetectParser parser) {
		this.parser = parser;
	}

	public Set<MediaType> getSupportedTypes(ParseContext context) {
		// Return what you want to handle
		HashSet<MediaType> types = new HashSet<MediaType>();
		types.add(MediaType.application("pdf"));
		types.add(MediaType.application("zip"));
		// return types;
		return parser.getSupportedTypes(context);
	}

	public void parse(InputStream stream, ContentHandler handler,
			Metadata metadata, ParseContext context) throws IOException {
		// Do something with the child documents
		// eg save to disk
		File f = File.createTempFile("tika", "tmp");
		found.add(f);

		FileOutputStream fout = new FileOutputStream(f);
		IOUtils.copy(stream, fout);
		fout.close();
	}

}
