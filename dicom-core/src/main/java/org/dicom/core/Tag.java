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

	@Override
	public int hashCode() {
		return ((groupId & 0X0000FFFF) << 16) | (elementNumber & 0X0000FFFF);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Tag))
			return false;
		Tag a = (Tag) obj;
		return a.getGroupId() == getGroupId() && a.getElementNumber() == getElementNumber();
	}

	@Override
	public String toString() {
		return String.format("(%04x, %04x)", groupId, elementNumber);
	}
}
