package org.cytoscape.PTMOracle.internal.util.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

/**
 * Extension of DefaultComboModel that we can tweak.
 * @author aidan
 */
public class ComboBoxDisplayModel extends DefaultComboBoxModel<String> {

	private static final long serialVersionUID = 1410579085983149705L;
	
	public ComboBoxDisplayModel(Vector<String> values) {
		super(values);
	}
	
	public List<String> getDataVector() {
		Set<String> dataVector = new HashSet<String>();
		for (int i = 0; i < getSize(); i++) {
			dataVector.add(getElementAt(i));
		}
		
		List<String> sortedDataVector = new ArrayList<String>(dataVector);
		Collections.sort(sortedDataVector);
		return sortedDataVector;
	}
	
	public void insertElementInOrder(String newElement) {
		for (int i = 0; i < getSize(); i++) {
			String currElement = (String) getElementAt(i);
			
			if (String.CASE_INSENSITIVE_ORDER.compare(currElement, newElement) > 0) {
				insertElementAt(newElement, i);
				return;
			}
		}
		addElement(newElement);
	}

}