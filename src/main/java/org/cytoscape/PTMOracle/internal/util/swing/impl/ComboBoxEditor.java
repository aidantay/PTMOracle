package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Special table editor for displaying comboboxes in a table.
 * @author aidan
 */
public class ComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
		
	private static final long serialVersionUID = 8890967479952838599L;
	
	private JComboBox<String> comboBox;
		
	public ComboBoxEditor() {
		super();
	}

	@Override
	public Object getCellEditorValue() {
		return comboBox;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		// Set the contents of the table cell to the comboBox we have put into the table
		comboBox = (JComboBox<String>) value;
		return comboBox;
	}
}
	
