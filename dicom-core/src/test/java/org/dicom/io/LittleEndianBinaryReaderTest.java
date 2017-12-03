package org.dicom.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.dicom.io.LittleEndianBinaryReader;
import org.junit.Assert;

public class LittleEndianBinaryReaderTest {
	LittleEndianBinaryReader target;

	@Test
	public void testReadInt() throws IOException {
		try (InputStream inputs = new ByteArrayInputStream(new byte[] { 0x01, 0x22, (byte) 0xF8, 0x3E })) {
			target = new LittleEndianBinaryReader(inputs);
			int value = target.readInt();
			Assert.assertEquals(0x3EF82201, value);
		}
		try (InputStream inputs = new ByteArrayInputStream(
				new byte[] { (byte) 0xFF, 0x22, (byte) 0xF8, (byte) 0xFF, 0x77, (byte) 0x88 })) {
			target = new LittleEndianBinaryReader(inputs);
			int value = target.readInt();
			Assert.assertEquals(0xFFF822FF, value);
		}
		try (InputStream inputs = new ByteArrayInputStream(new byte[] { (byte) 0xFF })) {
			target = new LittleEndianBinaryReader(inputs);
			int value = target.readInt();
			Assert.assertEquals(0xFF, value);
		}
		try (InputStream inputs = new ByteArrayInputStream(new byte[] { (byte) 0xFF, (byte) 0xF8 })) {
			target = new LittleEndianBinaryReader(inputs);
			int value = target.readInt();
			Assert.assertEquals(0xF8FF, value);
		}
	}

	@Test
	public void testReadShort() throws IOException {
		try (InputStream inputs = new ByteArrayInputStream(new byte[] { 0x01, 0x22, (byte) 0xF8, 0x3E })) {
			target = new LittleEndianBinaryReader(inputs);
			short value = target.readShort();
			Assert.assertEquals(0x2201, value);
		}
		try (InputStream inputs = new ByteArrayInputStream(new byte[] { (byte) 0xFF })) {
			target = new LittleEndianBinaryReader(inputs);
			short value = target.readShort();
			Assert.assertEquals(0xFF, value);
		}
		try (InputStream inputs = new ByteArrayInputStream(new byte[] { (byte) 0xFF, (byte) 0xF8 })) {
			target = new LittleEndianBinaryReader(inputs);
			short value = target.readShort();
			Assert.assertEquals((short) 0xF8FF, value);
		}
	}

	public void testReadLong() throws IOException {
		try (InputStream inputs = new ByteArrayInputStream(new byte[] { 0x01, 0x22, (byte) 0xF8, 0x3E })) {
			target = new LittleEndianBinaryReader(inputs);
			long value = target.readLong();
			Assert.assertEquals(0x3EF82201L, value);
		}
		try (InputStream inputs = new ByteArrayInputStream(new byte[] { 0x01, 0x22, (byte) 0xF8, 0x3E, (byte) 0xFF,
				(byte) 0x88, (byte) 0x99, (byte) 0xFa, (byte) 0xbb })) {
			target = new LittleEndianBinaryReader(inputs);
			long value = target.readLong();
			Assert.assertEquals(0xFA9988FF3EF82201L, value);
		}
	}

	@Test
	public void testReadFloat() throws IOException {
		try (InputStream inputs = new ByteArrayInputStream(
				new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x7F })) {
			target = new LittleEndianBinaryReader(inputs);
			float value = target.readFloat();
			Assert.assertEquals(Float.POSITIVE_INFINITY, value, 1.0E-5);
		}
	}
}
