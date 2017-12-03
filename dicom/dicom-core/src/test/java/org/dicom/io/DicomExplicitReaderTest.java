package org.dicom.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

import org.dicom.core.DataElement;
import org.dicom.core.Tag;
import org.junit.Test;

public class DicomExplicitReaderTest {

	@Test
	public void testRead() throws IOException, ClassNotFoundException {
		try (DicomReader target = new DicomExplicitReader(new LittleEndianBinaryReader(
				new BufferedInputStream(this.getClass().getResourceAsStream("/image-000001.dcm"))))) {
			Map<Tag, DataElement> outs = target.read();
			System.out.println(outs);
		}
	}
}
