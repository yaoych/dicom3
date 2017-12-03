package org.dicom.core;

public class Tag {
	private int groupId;
	private int elementNumber;

	public Tag(int groupId, int elementNumber) {
		this.groupId = groupId;
		this.elementNumber = elementNumber;
	}

	public int getGroupId() {
		return groupId;
	}

	public int getElementNumber() {
		return elementNumber;
	}
}
