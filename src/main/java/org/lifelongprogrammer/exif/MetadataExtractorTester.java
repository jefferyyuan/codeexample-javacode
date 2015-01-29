package org.lifelongprogrammer.exif;

import java.io.File;
import java.util.Iterator;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class MetadataExtractorTester {
	public static void main(String[] args) {
//		if (args.length == 0) {
//			System.out.println("Usage: Test <image-file>");
//			System.exit(0);
//		}

		String filename = "E:/jeffery/tmp/images/13195632914_a32a024f50_o.jpg"; //args[0];
		System.out.println("Filename: " + filename);

		try {
			File jpgFile = new File(filename);
			Metadata metadata = ImageMetadataReader.readMetadata(jpgFile);

			Iterator<Directory> directories = metadata.getDirectories()
					.iterator();
			while (directories.hasNext()) {
				Directory directory = directories.next();
				// iterate through tags and print to System.out
				Iterator<Tag> tags = directory.getTags().iterator();
				while (tags.hasNext()) {
					Tag tag = tags.next();
					// use Tag.toString()
					System.out.println(tag);
				}
			}

			// // Read Exif Data
			// Directory directory = metadata
			// .getDirectory(ExifSubIFDDirectory.class);
			// if (directory != null) {
			//
			// // Read the date
			// Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME);
			// DateFormat df = DateFormat.getDateInstance();
			// df.format(date);
			// int year = df.getCalendar().get(Calendar.YEAR);
			// int month = df.getCalendar().get(Calendar.MONTH) + 1;
			//
			// System.out.println("Year: " + year + ", Month: " + month);
			//
			// System.out.println("Date: " + date);
			//
			// System.out.println("Tags");
			// for (Iterator i = directory.getTags().iterator(); i.hasNext();) {
			// Tag tag = (Tag) i.next();
			// System.out.println("\t" + tag.getTagName() + " = "
			// + tag.getDescription());
			//
			// }
			// } else {
			// System.out.println("EXIF is null");
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
