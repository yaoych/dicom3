package org.dicom.core;

import java.util.ArrayList;
import java.util.List;

public class DataSet extends DataElement {

	@SuppressWarnings("unchecked")
	public DataSet(Tag tag, String VR) {
		super(tag, VR, new ArrayList<>());
		elements = (List<DataElement>) getValue();
	}

	@SuppressWarnings("unchecked")
	public DataSet() {
		super(null, null, new ArrayList<DataElement>());
		elements = (List<DataElement>) getValue();
	}

	private List<DataElement> elements;

	public List<DataElement> getElements() {
		return elements;
	}
}
