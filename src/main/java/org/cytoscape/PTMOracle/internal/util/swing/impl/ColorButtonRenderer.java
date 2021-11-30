package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Special table renderer for displaying color buttons in a table.
 * @author aidan
 */
public class ColorButtonRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 2796955991320548211L;
	
	public ColorButtonRenderer() {
		super();
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if (value instanceof Color) {
			Color newColor = (Color) value;
			setBackground(newColor);
			setText("");
			
		} else {
			setBackground(Color.WHITE);
			setText((String) value);
		}

		return this;
	}

}
