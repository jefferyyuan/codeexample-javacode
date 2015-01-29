package org.lifelongprogrammer.textming;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;

public class TikaTester {

	// private static List<String> imageInteresedMeta = Lists.newArrayList(
	// "Model", "Lens", "Exposure Mode");

	private static Map<String, List<String>> imageProperties = new LinkedHashMap<String, List<String>>();

	static {
		imageProperties.put("Camera",
				Lists.newArrayList("Model", "tiff:Model", "Make", "tiff:Make"));
		imageProperties.put("Camera Lens",
				Lists.newArrayList("Lens", "Lens Model"));
		imageProperties.put("Lens Specification",
				Lists.newArrayList("Lens Specification"));
		imageProperties
				.put("Photo Taken at", Lists.newArrayList("Date/Time Original",
						"exif:DateTimeOriginal", "Creation-Date"));
		imageProperties.put("Last Modified at", Lists.newArrayList(
				"Last-Modified", "modified", "Last-Save-Date",
				"dcterms:modified"));

		imageProperties.put("Software", Lists.newArrayList("Software"));
		imageProperties.put("Version Info", Lists.newArrayList("Version Info"));

		imageProperties.put("Image Width", Lists.newArrayList("Image Width",
				"Exif Image Width", "tiff:ImageWidth"));
		imageProperties.put("Image Height", Lists.newArrayList("Image Height",
				"Exif Image Height", "tiff:ImageLength"));
		imageProperties.put("Resolution Info",
				Lists.newArrayList("Resolution Info"));
		imageProperties.put("Flash", Lists.newArrayList("Flash", "exif:Flash"));
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, org.xml.sax.SAXException, TikaException {

		String urlStr = "https://lh6.googleusercontent.com/9jTb0WWgL_25NMTa4bezENXwDL27CzCQD1xahwo5Nnk=s0-d";
		File file = new File(
				"E:/jeffery/tmp/images/13195632914_a32a024f50_o.jpg");
		// InputStream is = new FileInputStream(file);

		// InputStream is = new
		getImageMetaData(urlStr);
	}

	public static void getImageMetaData(String urlStr) throws IOException,
			MalformedURLException, SAXException, TikaException {
		byte[] bytes = IOUtils.toByteArray(new URL(urlStr));
		int countInBytes = bytes.length;
		System.out.println(countInBytes / 1024);
		System.out.println(countInBytes / (1024 * 1024));

		// Image image = Toolkit.getDefaultToolkit().createImage(bytes);
		//
		// ImageIcon icon = new ImageIcon(image);
		// System.out.println(icon.getIconWidth());
		// System.out.println(icon.getIconHeight());

		// new ImageIcon(image).gets

		// URLConnection conn = new URL(urlStr).openConnection();
		// InputStream is = conn.getInputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Metadata metadata = new Metadata();
		BodyContentHandler ch = new BodyContentHandler();
		AutoDetectParser parser = new AutoDetectParser();
		// Parser parser = new JpegParser();

		// String mimeType = new Tika().detect(file);
		// metadata.set(Metadata.CONTENT_TYPE, mimeType);

		parser.parse(bais, ch, metadata, new ParseContext());
		bais.close();

		Iterator<Entry<String, List<String>>> it = imageProperties.entrySet()
				.iterator();
		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		while (it.hasNext()) {
			Entry<String, List<String>> entry = it.next();
			for (String key : entry.getValue()) {
				String prop = metadata.get(key);
				if (StringUtils.isNotBlank(prop)) {
					resultMap.put(entry.getKey(), prop);
					System.out.println(entry.getKey() + ": " + prop);
					break;
				}
			}
		}

		System.out.println("********************8");

		for (int i = 0; i < metadata.names().length; i++) {
			String item = metadata.names()[i];
			System.out.println(item + " -- " + metadata.get(item));
		}

		System.out.println(ch.toString());
	}
}
