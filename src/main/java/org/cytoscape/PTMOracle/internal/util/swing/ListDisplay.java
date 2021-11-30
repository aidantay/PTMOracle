package org.cytoscape.PTMOracle.internal.util.swing;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 * ListDisplays are JLists used for single and/or multiple selection
 * @author aidan
 */
public class ListDisplay extends JList<String> {

	private static final long serialVersionUID = 1410579085983149705L;
	
	private List<?> conditions;				// List of conditions the display is dependent on.
	private ListDisplayModel listModel;
	
	public ListDisplay(List<?> conditions) {
		super();
		
		this.conditions = conditions;
		listModel = new ListDisplayModel(getValues());
		setModel(listModel);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setVisibleRowCount(7);
	}
	
	public ListDisplay() {
		this(Arrays.asList());
	}

	public List<?> getConditions() {
		return conditions;
	}
	
	public void setConditions(List<?> conditions) {
		this.conditions = conditions;

		Vector<String> newValues = getValues();
		ListDisplayModel model = (ListDisplayModel) getModel();
		
		model.removeAllElements();		
		for (String s : newValues) {
			model.insertElementInOrder(s);
		}
	}

	
	public ListDisplayModel getModel() {
		return listModel;
	}
	
	public Vector<String> getValues() {
		return null;
	}
	
}