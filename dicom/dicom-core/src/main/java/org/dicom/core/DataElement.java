package org.dicom.core;

import java.util.Arrays;
import java.util.List;

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

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public void setVR(String vR) {
		VR = vR;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setLen(int len) {
		this.len = len;
	}

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