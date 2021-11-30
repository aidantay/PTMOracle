package org.cytoscape.PTMOracle.internal.util.swing;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;

/**
 * Extension of DefaultListModel that we can tweak.
 * @author aidan
 */
public class ListDisplayModel extends DefaultListModel<String> {

	private static final long serialVersionUID = 1410579085983149705L;
	
	public ListDisplayModel(Vector<String> values) {
		super();
		
		if (values != null) {
			for (String s : values) {
				addElement(s);
			}
		}
	}

	public ListDisplayModel() {
		this(null);
	}
	
	public List<String> getDataVector() {
		List<String> dataVector = new ArrayList<String>();
		for (int i = 0; i < getSize(); i++) {
			dataVector.add(getElementAt(i));
		}
		return dataVector;
	}
	
	public void insertElementInOrder(String newElement) {
		for (int i = 0; i < getSize(); i++) {
			String currElement = (String) get(i);
			
			if (String.CASE_INSENSITIVE_ORDER.compare(currElement, newElement) > 0) {
				insertElementAt(newElement, i);
				return;		// So we don't have to continue iterating 
			}
		}
		addElement(newElement);
	}

}