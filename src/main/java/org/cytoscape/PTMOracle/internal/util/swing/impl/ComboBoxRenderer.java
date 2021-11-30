package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Special table renderer for displaying comboboxes in a table.
 * @author aidan
 */
public class ComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {

	private static final long serialVersionUID = 2796955991320548211L;
	
	public ComboBoxRenderer() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		JComboBox<String> comboBox = (JComboBox<String>) value;
		return comboBox;
	}

}
