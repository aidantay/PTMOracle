package org.cytoscape.PTMOracle.internal.util.swing;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * Extension of DefaultTableModel that we can tweak.
 * @author aidan
 */
public class TableDisplayModel extends DefaultTableModel {

	private static final long serialVersionUID = 2796955991320548211L;
	
	private Map<Integer, Boolean> columnEditableMap;
	private Map<Integer, Class<?>> columnClassMap;
	
	public TableDisplayModel(Vector<String> columnNames) {
		super(columnNames, 0);
		
		columnClassMap = new HashMap<Integer, Class<?>>();
		columnEditableMap = new HashMap<Integer, Boolean>();
	}
	
	public Map<Integer, Boolean> getColumnEditableMap() {
		return columnEditableMap;
	}
	
	public Map<Integer, Class<?>> getColumnClassMap() {
		return columnClassMap;
	}
	
	public void setColumnEditable(boolean isColumnEditable, int columnIndex) {
		getColumnEditableMap().put(columnIndex, isColumnEditable);
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return getColumnEditableMap().get(column);
	}
	
	public void setColumnClass(Class<?> clazz, int columnIndex) {
		getColumnClassMap().put(columnIndex, clazz);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (getColumnClassMap().containsKey(columnIndex)) {
			return getColumnClassMap().get(columnIndex);
		}
		return super.getColumnClass(columnIndex);
	}

}