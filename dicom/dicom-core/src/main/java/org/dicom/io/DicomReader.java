package org.dicom.io;

import java.io.IOException;
import java.util.ArrayList;

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

	public DataElement read() throws IOException {
		short groupId = binaryReader.readShort();
		short elementNumber = binaryReader.readShort();
		Tag tag = new Tag(groupId & 0X0000FFFF, elementNumber & 0X0000FFFF);
		String vr = new String(binaryReader.readBytes(2));
		int vl = 0;
		if (vr.startsWith("O") || vr.equals("SQ")) {
			binaryReader.readShort();// skip 000H reserved
			vl = binaryReader.readInt();
		} else {
			vl = binaryReader.readShort() & 0X0000FFFF;
		}
		if(vr.equals("SQ")) {
			ArrayList<DataElement> sq = new ArrayList<>();
			do {
				read();
			}
		}
		Object value = readValue(vr, vl);
		return new DataElement(tag, vr, value);
	}

	Object readValue(String VR, int len) throws IOException {
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

}
