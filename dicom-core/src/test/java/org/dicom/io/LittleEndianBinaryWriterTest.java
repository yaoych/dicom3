package org.dicom.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.dicom.io.LittleEndianBinaryWriter;
import org.junit.Assert;
import org.junit.Test;

public class LittleEndianBinaryWriterTest {

	@Test
	public void testWriteInt() throws IOException {
		try (ByteArrayOutputStream outputs = new ByteArrayOutputStream()) {
			try (LittleEndianBinaryWriter writer = new LittleEndianBinaryWriter(outputs)) {
				writer.writeInt(0XFF080900);
				byte[] bytes = outputs.toByteArray();
				Assert.assertArrayEquals(new byte[] { 0X00, 0X09, 0X08, (byte) 0XFF }, bytes);
			}
		}
	}

	public void testWriteFloat() throws IOException {
		try (ByteArrayOutputStream outputs = new ByteArrayOutputStream()) {
			try (LittleEndianBinaryWriter writer = new LittleEndianBinaryWriter(outputs)) {
				writer.writeFloat(Float.POSITIVE_INFINITY);
				byte[] bytes = outputs.toByteArray();
				Assert.assertArrayEquals(new byte[] { 0X00, 0X00, 0X00, (byte) 0X80, 0X7F }, bytes);
			}
		}
	}
}
