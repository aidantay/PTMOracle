package org.cytoscape.PTMOracle.internal.util.swing;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;

/**
 * ComboBoxDisplays are JComboboxes used for single selection. 
 * @author aidan
 */
public class ComboBoxDisplay extends JComboBox<String> {

	private static final long serialVersionUID = 2818428841993341392L;

	private List<?> conditions;					// List of conditions the display is dependent on.
	private ComboBoxDisplayModel comboBoxModel;
	
	public ComboBoxDisplay(List<?> conditions) {
		super();

		this.conditions = conditions;
		comboBoxModel = new ComboBoxDisplayModel(getValues());
		setModel(comboBoxModel);
	}
	
	public ComboBoxDisplay() {
		this(Arrays.asList());
	}

	public List<?> getConditions() {
		return conditions;
	}
	
	public void setConditions(List<?> conditions) {
		this.conditions = conditions;

		Vector<String> newValues = getValues();
		ComboBoxDisplayModel model = (ComboBoxDisplayModel) getModel();
		
		model.removeAllElements();		
		for (String s : newValues) {
			model.insertElementInOrder(s);
		}
	}
	
	public String getSelectedItem() {
		return (String) super.getSelectedItem();
	}

	public Vector<String> getValues() {
		return null;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append("[");
		for (int i = 0; i < getItemCount(); i++) {
			out.append(getItemAt(i));
			if (i + 1 < getItemCount()) {
				out.append(",");
			}
		}
		out.append("]");
		return out.toString();	
	}

}
