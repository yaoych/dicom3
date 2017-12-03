package org.dicom.core;

public class DataElement {

	public DataElement(Tag tag, String VR, Object value, int len) {
		this.tag = tag;
		this.VR = VR;
		this.value = value;
		this.len = len;
	}

	private Tag tag;
	private String VR;
	private Object value;
	private int len;

	public String getVR() {
		return VR;
	}

	public Object getValue() {
		return value;
	}

	public int getLen() {
		return len;
	}

	public Tag getTag() {
		return tag;
	}
}
