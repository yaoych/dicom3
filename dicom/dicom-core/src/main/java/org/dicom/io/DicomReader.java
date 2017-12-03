package org.dicom.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dicom.core.DataElement;
import org.dicom.core.Tag;
import org.dicom.core.VRType;

public class DicomReader {

	public DicomReader(BinaryReader binaryReader) {
		this.binaryReader = binaryReader;
	}

	BinaryReader binaryReader;
	String charset = "utf-8";
	boolean isExplicit = true;

	// read element
	public DataElement readElement() throws IOException {
		Tag tag = readTag();
		if (SQ_END_TAG.equals(tag) || ITEM_TAG.equals(tag) || ITEM_END_TAG.equals(tag)) {
			int len = binaryReader.readInt();
			return new DataElement(tag, null, null, len);
		}
		String vr = readVR();
		int len = readLength(vr);
		Object value = null;
		if (!vr.equals("SQ")) {
			if (len < 0)
				throw new IOException("value length is too big to read");
			value = readValue(vr, len);
		} else {
			if (len < 0) {
				value = readNestedDataSetWithoutLen();
			} else {
				value = readNestedDataSetWithLen(len);
			}
		}
		return new DataElement(tag, vr, value, len);
	}

	private Map<Tag, DataElement> readGroup(int len) throws IOException {
		int size = len;
		Map<Tag, DataElement> elems = new TreeMap<>();
		while (size > 0) {
			DataElement elem = readElement();
			elems.put(elem.getTag(), elem);
			size -= getSize(elem);
		}
		assert size == 0;
		return elems;
	}

	public static final Tag ITEM_TAG = new Tag(0XFFFE, 0XE000);
	public static final Tag ITEM_END_TAG = new Tag(0XFFFE, 0XE00D);
	public static final Tag SQ_END_TAG = new Tag(0XFFFE, 0XE0DD);

	private List<Map<Tag, DataElement>> readNestedDataSetWithoutLen() throws IOException {
		List<Map<Tag, DataElement>> dataSet = new ArrayList<>();
		DataElement elem = readElement();
		while (elem.getTag().equals(ITEM_TAG)) {
			if (elem.getLen() > 0) {
				Map<Tag, DataElement> group = readGroup(elem.getLen());
				dataSet.add(group);
			} else {
				Map<Tag, DataElement> group = new TreeMap<>();
				elem = readElement();
				while (!elem.getTag().equals(ITEM_END_TAG)) {
					group.put(elem.getTag(), elem);
				}
				dataSet.add(group);
			}
		}
		assert elem.getTag().equals(SQ_END_TAG);
		return dataSet;
	}

	private List<Map<Tag, DataElement>> readNestedDataSetWithLen(int len) throws IOException {
		List<Map<Tag, DataElement>> dataSet = new ArrayList<>();
		DataElement elem = readElement();
		while (len > 0) {
			if (elem.getLen() > 0) {
				Map<Tag, DataElement> group = readGroup(elem.getLen());
				dataSet.add(group);
				len -= getSize(elem);
			} else {
				Map<Tag, DataElement> group = new TreeMap<>();
				elem = readElement();
				len -= getSize(elem);
				while (!elem.getTag().equals(ITEM_END_TAG)) {
					group.put(elem.getTag(), elem);
				}
				dataSet.add(group);
			}
		}
		assert len == 0;
		return dataSet;
	}

	private Tag readTag() throws IOException {
		short groupId = binaryReader.readShort();
		short elementNumber = binaryReader.readShort();
		Tag tag = new Tag(groupId & 0X0000FFFF, elementNumber & 0X0000FFFF);
		return tag;
	}

	private Object readValue(String VR, int len) throws IOException {
		VRType type = VRType.of(VR);
		switch (type) {
		case String:
			return new String(binaryReader.readBytes(len));
		case Text:
			return new String(binaryReader.readBytes(len), 0, len, charset);
		case Short:
			return binaryReader.readShort();
		case UShort:
			return binaryReader.readShort() & 0X0000FFFF;
		case Int:
			return binaryReader.readInt();
		case UInt:
			return binaryReader.readInt() & 0XFFFFFFFFL;
		case Float:
			return binaryReader.readFloat();
		case Double:
			return binaryReader.readDouble();
		case Bytes:
			return binaryReader.readBytes(len);
		case Shorts:
			int size = len / BinaryReader.SIZE_OF_SHORT;
			short[] ws = new short[size];
			for (int i = 0; i < size; i++) {
				ws[i] = binaryReader.readShort();
			}
			return ws;
		case Ints:
			size = len / BinaryReader.SIZE_OF_INT;
			int[] ints = new int[size];
			for (int i = 0; i < size; i++) {
				ints[i] = binaryReader.readInt();
			}
			return ints;
		case Floats:
			size = len / BinaryReader.SIZE_OF_INT;
			float[] fs = new float[size];
			for (int i = 0; i < size; i++) {
				fs[i] = binaryReader.readFloat();
			}
			return fs;
		case Doubles:
			size = len / BinaryReader.SIZE_OF_LONG;
			double[] ds = new double[size];
			for (int i = 0; i < size; i++) {
				ds[i] = binaryReader.readDouble();
			}
			return ds;
		default:
			return null;
		}
	}

	private String readVR() throws IOException {
		return new String(binaryReader.readBytes(2));
	}

	private int readLength(String VR) throws IOException {
		if (isVeryLongValue(VR)) {
			binaryReader.readShort();
			return binaryReader.readInt();
		} else {
			return binaryReader.readShort();
		}

	}

	private List<String> veryLongVRs = Arrays.asList("OB", "OD", "OF", "OL", "OW", "SQ", "UC", "UR", "UT", "UN");

	private boolean isVeryLongValue(String VR) {
		return veryLongVRs.contains(VR);
	}

	private int getSize(DataElement elem) {
		int len = 0;
		if (elem.getTag() != null)
			len += 4;// tag
		if (elem.getVR() != null) {
			len += 2;// vr
			if (isVeryLongValue(elem.getVR()))
				len += 6;// 000H and 4 bytes of length
			else
				len += 2;// len of length
		} else {
			len += 4;// implicit length stands 4 bytes
		}
		if (elem.getLen() > 0) {
			len += elem.getLen();// value length
		}
		return len;
	}
}