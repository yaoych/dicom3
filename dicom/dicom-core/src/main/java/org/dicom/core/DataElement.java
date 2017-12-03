package org.dicom.core;

public class DataElement {

	public DataElement(Tag tag, String VR, Object value) {
		this.tag = tag;
		this.VR = VR;
		this.value = value;
	}

	private Tag tag;
	private String VR;
	private Object value;

	public Tag getTag() {
		return tag;
	}

	public String getVR() {
		return VR;
	}

	public Object getValue() {
		return value;
	}

}
