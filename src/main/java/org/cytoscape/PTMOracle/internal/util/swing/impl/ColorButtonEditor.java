package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Special table editor for displaying color buttons in a table.
 * @author aidan
 */
public class ColorButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
		
	private static final long serialVersionUID = 8890967479952838599L;
	private static final String EDIT = "edit";
	
	private Color currentColor;
	private JButton button;
	private JColorChooser colorChooser;
	private JDialog dialog;
		
	public ColorButtonEditor() {
		super();
		
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		button.setBorderPainted(false);
		
		colorChooser = new JColorChooser();
		dialog = JColorChooser.createDialog(button, "Pick a Color", true, colorChooser, this, null);
	}

	@Override
	public Object getCellEditorValue() {
		return currentColor;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
			
		// Set the contents of the table cell to the button we have put into the table
		currentColor = (Color) value;		
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
			button.setBackground(currentColor);
			colorChooser.setColor(currentColor);
			dialog.setVisible(true);
			
			fireEditingStopped();
		} else {
			currentColor = colorChooser.getColor();
		}
		
	}
}
	
