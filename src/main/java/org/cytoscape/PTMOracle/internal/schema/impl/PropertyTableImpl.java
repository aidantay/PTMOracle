package org.cytoscape.PTMOracle.internal.schema.impl;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.cytoscape.PTMOracle.internal.schema.AbstractOracleTable;
import org.cytoscape.PTMOracle.internal.schema.PropertyTable;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;

/**
 * Table of acceptable properties. 
 * Users can add, remove or modify properties if necessary 
 * Table stores:
 *  - Property type (PrimaryKey)
 *  - Column name of property
 *  - Colour representation of the property used by OracleResults
 *  - Whether property is for nodes or edges
 *  - Whether property is interval
 *  - Whether property is mergeable 
 *  - Whether property is represented as a list or boolean 
 *  - Whether property is default. Default properties can ONLY be edited/modified
 * @author aidan
 */
public class PropertyTableImpl extends AbstractOracleTable implements PropertyTable {

	public PropertyTableImpl(CyTableFactory tableFactory) {
		CyTable table = tableFactory.createTable(getTableName(), TYPE, String.class, false, true);
		table.createColumn(COLUMN_NAME,      String.class,  false);
		table.createColumn(COLOR,            String.class,  false);
		table.createColumn(IS_NODE_PROPERTY, Boolean.class, false);
		table.createColumn(IS_INTERVAL,      Boolean.class, false);
		table.createColumn(IS_MERGEABLE,     Boolean.class, false);
		table.createColumn(IS_COLUMN_LIST,   Boolean.class, false);
		table.createColumn(IS_DEFAULT,       Boolean.class, false);
		setTable(table);
		setColumnNames(Arrays.asList(TYPE, COLUMN_NAME, COLOR, IS_NODE_PROPERTY, IS_INTERVAL, IS_MERGEABLE, IS_COLUMN_LIST, IS_DEFAULT));

		insertRow(Arrays.asList(SEQUENCE, SEQUENCE_COLUMN, "#000000", true,  false, false, false, true));
		insertRow(Arrays.asList(PTM,      PTM_COLUMN,      "#e31a1c", true,  false, false, true,  true));
		insertRow(Arrays.asList(MOTIF,    MOTIF_COLUMN,    "#1f78b4", true,  true,  false, true,  true));
		insertRow(Arrays.asList(DOMAIN,   DOMAIN_COLUMN,   "#33a02c", true,  true,  false, true,  true));
		insertRow(Arrays.asList(DISORDER, DISORDER_COLUMN, "#6a3d9a", true,  true,  true,  false, true));
//		insertRow(Arrays.asList(INT_RES,  false, INT_RES_COLUMN,  "#FFC800", false, false, false, true));
//		insertRow(Arrays.asList(DDI,      false, DDI_COLUMN,      "#FF00FF", false, true,  true,  true));
//		insertRow(Arrays.asList(DMI,      false, DMI_COLUMN,      "#FFC800", false, true,  true,  true));
	}
	
	@Override
	public String getTableName() {
		return "Properties";
	}

	@Override
	public void insertRow(List<?> rowValues) {
		if (rowValues.size() != getTable().getColumns().size()) {
			throw new IllegalArgumentException("Unable to insert row. Invalid number of columns\t" + getTableName());
		}

		String propertyType    = (String)  rowValues.get(0);
		String columnName      = (String)  rowValues.get(1);
		String color           = (String)  rowValues.get(2);
		Boolean isNodeProperty = (Boolean) rowValues.get(3);
		Boolean isInterval     = (Boolean) rowValues.get(4);
		Boolean isMergeable    = (Boolean) rowValues.get(5);
		Boolean isList         = (Boolean) rowValues.get(6);
		Boolean isDefault      = (Boolean) rowValues.get(7);
		
		if (hasRow(propertyType)) {
			throw new KeyAlreadyExistsException("Unable to insert row. Primary key exists\t" + getTableName());
		}

		CyRow row = getTable().getRow(propertyType);
		row.set(COLUMN_NAME, columnName);
		row.set(COLOR, color);
		row.set(IS_NODE_PROPERTY, isNodeProperty);
		row.set(IS_INTERVAL, isInterval);
		row.set(IS_MERGEABLE, isMergeable);
		row.set(IS_COLUMN_LIST, isList);
		row.set(IS_DEFAULT, isDefault);
	}
	
	@Override
	public boolean hasRow(Object primaryKey) {
		String type = (String) primaryKey;
		CyColumn column = getTable().getColumn(TYPE);
		return column.getValues(String.class).contains(type);
	}
	
	@Override
	public CyRow getRow(Object primaryKey) {
		String type = (String) primaryKey;
		Collection<CyRow> rows = getTable().getMatchingRows(TYPE, type);
		for (CyRow row : rows) {
			return row;
		}
		return null;
	}
	
	@Override
	public String getPropertyColumnName(String type) {
		return getRow(type).get(COLUMN_NAME, String.class);
	}

	@Override
	public Color getPropertyColour(String type) {		
		return Color.decode(getRow(type).get(COLOR, String.class));
	}

	@Override
	public boolean getNodePropertyFlag(String type) {
		return getRow(type).get(IS_NODE_PROPERTY, Boolean.class);
	}
	
	@Override
	public boolean getIntervalFlag(String type) {
		return getRow(type).get(IS_INTERVAL, Boolean.class);
	}
	
	@Override
	public boolean getMergeableFlag(String type) {
		return getRow(type).get(IS_MERGEABLE, Boolean.class);
	}
	
	@Override
	public boolean getListFlag(String type) {
		return getRow(type).get(IS_COLUMN_LIST, Boolean.class);
	}
	
	@Override
	public boolean getDefaultFlag(String type) {
		return getRow(type).get(IS_DEFAULT, Boolean.class);
	}

}