package org.cytoscape.PTMOracle.internal.schema;

import java.util.List;
import java.util.Set;

import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

/**
 * OracleTables are individual tables that store information. 
 * These include: Properties, Accepted modification-regex pairings, 
 * Accepted properties, etc...
 * @author aidan
 */
public interface OracleTable {

	/*
	 * Table column names
	 */
	public static final String ID                = "ID";
	public static final String ROOT_NETWORK_NAME = "RootNetworkName";
	public static final String SOURCE            = "Source";
	
	public static final String SHARED_NODE_NAME  = "SharedNodeName";
	public static final String TYPE              = "Type";
	public static final String DESCRIPTION       = "Description";
	public static final String START_POSITION    = "StartPosition";
	public static final String END_POSITION      = "EndPosition";
	public static final String RESIDUE           = "Residue";
	public static final String STATUS            = "Status";
	public static final String COMMENTS          = "Comments";

	public static final String SHARED_EDGE_NAME      = "SharedEdgeName";
	public static final String SOURCE_START_POSITION = "SourceStartPosition";
	public static final String SOURCE_END_POSITION   = "SourceEndPosition";
	public static final String TARGET_START_POSITION = "TargetStartPosition";
	public static final String TARGET_END_POSITION   = "TargetEndPosition";

	public static final String KEYWORD           = "Keyword";
	public static final String MODIFICATION      = "Modification";
	public static final String REGEX             = "RegexPattern";
	
	public static final String IS_NODE_PROPERTY  = "isNodeProperty";
	public static final String IS_INTERVAL       = "isInterval";
	public static final String IS_MERGEABLE      = "isMergable";
	public static final String COLOR             = "Color";
	public static final String COLUMN_NAME       = "ColumnName";
	public static final String IS_COLUMN_LIST    = "isList";
	public static final String IS_DEFAULT        = "isDefault";

	public CyTable getTable();
	public String getTableName();
	public List<String> getColumnNames();

	public void setColumnNames(List<String> columnNames);
	public void setTable(CyTable table);
	
	public void insertRow(List<?> rowValues);
	public void deleteRows(List<?> primaryKeys);
	public boolean hasRow(Object primaryKey);
	public CyRow getRow(Object primaryKey);
	public List<CyRow> getAllRows();
	public Set<?> getPrimaryKeys();
	
}
