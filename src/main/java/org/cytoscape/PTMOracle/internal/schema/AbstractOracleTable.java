package org.cytoscape.PTMOracle.internal.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

/**
 * Abstract implementation of an OracleTable.
 * OracleTables are individual tables that store information. 
 * These include: Properties, Accepted modification-regex pairings, 
 * Accepted properties, etc...
 * @author aidan
 */
public abstract class AbstractOracleTable implements OracleTable {

	private List<String> columnNames;
	private CyTable table;
	
	public AbstractOracleTable() {
	}
	
	@Override
	public CyTable getTable() {
		return table;
	}
	
	@Override
	public void setTable(CyTable table) {
		this.table = table;
	}
	
	@Override
	public void setColumnNames(List<String> columnNames) {
		if (columnNames.size() != getTable().getColumns().size()) {
			throw new AssertionError("Unequal number of columns\t" + getTableName());
		}
		this.columnNames = columnNames;
	}
	
	@Override
	public List<String> getColumnNames() {
		return columnNames; 
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<?> getPrimaryKeys() {
		Class<?> primaryKeyClass = getTable().getPrimaryKey().getType();
		List<?> primaryKeys = getTable().getPrimaryKey().getValues(primaryKeyClass);
		return new HashSet(primaryKeys);
	}
	
	@Override
	public List<CyRow> getAllRows() {
		Class<?> primaryKeyClass = getTable().getPrimaryKey().getClass();
		List<?> primaryKeys      = getTable().getPrimaryKey().getValues(primaryKeyClass);
		
		List<CyRow> rows = new ArrayList<CyRow>();
		
		for (Object primaryKey : primaryKeys) {
			CyRow row = getRow(primaryKey);
			rows.add(row);
		}
		return rows;
	}
		
	@Override
	@SuppressWarnings("unchecked")
	public void deleteRows(List<?> primaryKeys) {
		List<Integer> ids = (List<Integer>) primaryKeys;
		getTable().deleteRows(ids);		
	}
	
	// Gets the index of the last row in a table
	// ONLY WORKS FOR TABLES WITH AN ID (INTEGER) PRIMARY KEY
	public int getLastRowIndex() {
		
		int id;
		List<Integer> ids = getTable().getPrimaryKey().getValues(Integer.class);
		
		// If there's no values, then the index of the last row last value is -1.
		if (ids.isEmpty()) {
			id = -1;
			
		// Otherwise, get the index of the last row.
		} else {
			Collections.sort(ids);
			id = ids.get(ids.size() - 1);
		}
		
		return id;
	}

	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		
		out.append(getTableName());
		out.append(System.getProperty("line.separator"));
		out.append(getColumnNames());
		out.append(System.getProperty("line.separator"));
		
		for (CyRow row : getTable().getAllRows()) {
			out.append(row.getAllValues());
			out.append(System.getProperty("line.separator"));
		}
		return out.toString();
	}

}
