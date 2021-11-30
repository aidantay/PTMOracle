package org.cytoscape.PTMOracle.internal.util.swing;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.cytoscape.PTMOracle.internal.util.swing.impl.TableColumnStringComparator;

/**
 * TableDisplays are JTables used for displaying information. 
 * @author aidan
 */
public class TableDisplay extends JTable {

	private static final long serialVersionUID = 5934260151591051503L;
	private static final int ROW_HEIGHT = 25;
	private static final int COLUMN_WIDTH = 100;

	private TableDisplayModel tableModel;

	public TableDisplay() {

		tableModel = new TableDisplayModel(getColumnValues());

		setModel(tableModel);
		setFillsViewportHeight(true);
		setAutoCreateRowSorter(true);
		setRowHeight(ROW_HEIGHT);
		setCellSelectionEnabled(true);
		setTableEditability(false);
		setMinColumnsWidth(COLUMN_WIDTH);
		setRowSorter(getTableSorter());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public TableDisplayModel getModel() {
		return tableModel;
	}
	
	// These functions should be overridden by subclasses if necessary
	public Vector<String> getColumnValues() {
		return null;
	}

	public void setRowValues() {

	}
	
	public TableRowSorter<TableDisplayModel> getTableSorter() {
		TableRowSorter<TableDisplayModel> sorter = new TableRowSorter<TableDisplayModel>(getModel());
		for (int i = 0; i < getColumnCount(); i++) {
			sorter.setComparator(i, new TableColumnStringComparator());			
		}
		return sorter;
	}
	
	public boolean isContentsValid() {
		return true;
	}
	
	public void updateOracle() {
		
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getDefaultRow() {
		return null;
	}
	
	// Additional table setting functions
	public void setTableEditability(boolean aFlag) {
		for (int columnIndex = 0; columnIndex < getModel().getColumnCount(); columnIndex++) {
			setColumnEditability(aFlag, columnIndex);
		}
	}
	
	public void setColumnEditability(boolean aFlag, int columnIndex) {
		getModel().setColumnEditable(aFlag, columnIndex);
	}
	
	public void setColumnClass(Class<?> clazz, int columnIndex) {
		getModel().setColumnClass(clazz, columnIndex);
	}
	
	public void setMinColumnsWidth(int width) {
		for (String columnName : getColumnValues()) {
			TableColumn column = getColumn(columnName);
			column.setMinWidth(width);
		}
	}
	
	public void setColumnEditor(TableCellEditor cellEditor, int columnIndex) {
		TableColumn column = getColumnModel().getColumn(columnIndex);
		column.setCellEditor(cellEditor);
	}
	
	public void setColumnRenderer(TableCellRenderer cellRenderer, int columnIndex) {
		TableColumn column = getColumnModel().getColumn(columnIndex);
		column.setCellRenderer(cellRenderer);		
	}
	
	// Input validators may put these things into a global Validation class
	public boolean isValidBoolean(String stringValue) {
		Pattern pattern = Pattern.compile("^true$|^false$");
	   	Matcher matcher = pattern.matcher(stringValue.toLowerCase());
	   	if (!matcher.find()) {
			return false;
	   	}
		return true;
	}
	
	public boolean isValidHexCode(String stringValue) {
		Pattern pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
	   	Matcher matcher = pattern.matcher(stringValue.toLowerCase());
	   	if (!matcher.find()) {
			return false;
	   	}
		return true;
	}
	
	public boolean isValidCellString(String stringValue) {
		if (stringValue == null || stringValue.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public void confirmTableDisplay() {
		// If a user leaves the last cell edited in a selected state, and clicks a button,
		// the data in that cell is taken as null, so the data for that cell is not saved.
		// Therefore, we need to remove the focus from the cell to another....Seems to work now.
		editCellAt(-1, -1);
		getSelectionModel().clearSelection();
		getModel().fireTableDataChanged();
	}
	
}
