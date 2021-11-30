package org.cytoscape.PTMOracle.internal.schema.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.model.property.NodePropertyImpl;
import org.cytoscape.PTMOracle.internal.model.property.PropertyMapImpl;
import org.cytoscape.PTMOracle.internal.schema.AbstractOracleTable;
import org.cytoscape.PTMOracle.internal.schema.NodePropertyTable;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;

/**
 * Table of node properties that are imported from file 
 * Table stores:
 *  - ID (Primary Key)
 *  - Name of the file source (The file where the property is from)
 *  - Shared Node Name (Shared name of the node)
 *  - PropertyID (The ID of the property)
 *  - Type
 *  - Description
 *  - Start position
 *  - End position
 *  - Residue
 *  - Status
 *  - Other comments
 * @author aidan
 */
public class NodePropertyTableImpl extends AbstractOracleTable implements NodePropertyTable {

	public NodePropertyTableImpl(CyTableFactory tableFactory) {
		CyTable table = tableFactory.createTable(getTableName(), ID, Integer.class, false, true);
		table.createColumn(SOURCE, String.class, false);
		table.createColumn(SHARED_NODE_NAME, String.class, false);
		table.createColumn(TYPE, String.class, false);
		table.createColumn(DESCRIPTION, String.class, false);
		table.createColumn(START_POSITION, Integer.class, false);
		table.createColumn(END_POSITION, Integer.class, false);
		table.createColumn(RESIDUE, String.class, false);
		table.createColumn(STATUS, String.class, false);
		table.createColumn(COMMENTS, String.class, false);
		setTable(table);
		setColumnNames(Arrays.asList(ID, SOURCE, SHARED_NODE_NAME, TYPE, DESCRIPTION, START_POSITION, END_POSITION, RESIDUE, STATUS, COMMENTS));
	}
	
	@Override
	public String getTableName() {
		return "NodeProperties";
	}
	
	@Override
	public void insertRow(List<?> rowValues) {
		String source         = (String)       rowValues.get(0);
		String sharedNodeName = (String)       rowValues.get(1);
		NodeProperty property = (NodeProperty) rowValues.get(2);
		int id = getLastRowIndex() + 1;
		
		if (hasRow(Arrays.asList(source, sharedNodeName, property))) {
			throw new KeyAlreadyExistsException("Unable to insert row. Primary key exists\t" + getTableName());
		}

		CyRow row = getTable().getRow(id);
		row.set(SOURCE, source);
		row.set(SHARED_NODE_NAME, sharedNodeName);
		row.set(TYPE, property.getType());
		row.set(DESCRIPTION, property.getDescription());
		row.set(START_POSITION, property.getStartPosition());
		row.set(END_POSITION, property.getEndPosition());
		row.set(RESIDUE, property.getResidue());
		row.set(STATUS, property.getStatus());
		row.set(COMMENTS, property.getComments());
	}
	
	@Override
	public boolean hasRow(Object primaryKey) {
		List<?> compositeKey   = (List<?>) primaryKey;
		String source          = (String) compositeKey.get(0);
		String sharedNodeName  = (String) compositeKey.get(1);
		NodeProperty property  = (NodeProperty) compositeKey.get(2);
		Collection<CyRow> rows = getTable().getMatchingRows(SOURCE, source);

		for (CyRow row : rows) {
			if (row.get(SHARED_NODE_NAME, String.class).equals(sharedNodeName)) {
				if (hasNodeProperty(row, property)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasNodeProperty(CyRow row, NodeProperty property) {
		String type        = (String)  row.getRaw(TYPE);
		String description = (String)  row.getRaw(DESCRIPTION);
		int startPosition  = (Integer) row.getRaw(START_POSITION);
		int endPosition    = (Integer) row.getRaw(END_POSITION);
		String residue     = (String)  row.getRaw(RESIDUE);
		String status      = (String)  row.getRaw(STATUS);
		String comments    = (String)  row.getRaw(COMMENTS);
		
		if (property.getType().equals(type) &&
			property.getStartPosition() == startPosition &&
			property.getEndPosition() == endPosition &&
			property.getDescription().equals(description) &&
			property.getResidue().equals(residue) &&
			property.getStatus().equals(status) &&
			property.getComments().equals(comments)) {
			return true;
		}
		return false;
	}
		
	@Override
	public CyRow getRow(Object primaryKey) {
		List<?> compositeKey   = (List<?>) primaryKey;
		String source          = (String)       compositeKey.get(0);
		String sharedNodeName  = (String)       compositeKey.get(1);
		NodeProperty property  = (NodeProperty) compositeKey.get(2);
		Collection<CyRow> rows = getTable().getMatchingRows(SOURCE, source);

		for (CyRow row : rows) {
			if (row.get(SHARED_NODE_NAME, String.class).equals(sharedNodeName)) {
				if (hasNodeProperty(row, property)) {
					return row;
				}
			}
		}
		return null;
	}
	
	@Override
	public PropertyMap getProperties(List<String> compositeKey) {
		String source          = (String) compositeKey.get(0);
		String sharedNodeName  = (String) compositeKey.get(1);		
		PropertyMap properties = new PropertyMapImpl();
		Collection<CyRow> rows = getTable().getMatchingRows(SOURCE, source);
		
		for (CyRow row : rows) {
			if (row.get(SHARED_NODE_NAME, String.class).equals(sharedNodeName)) {
				String type               = (String)  row.getRaw(TYPE);
				String description        = (String)  row.getRaw(DESCRIPTION);
				int startPosition         = (Integer) row.getRaw(START_POSITION);
				int endPosition           = (Integer) row.getRaw(END_POSITION);
				String residue            = (String)  row.getRaw(RESIDUE);
				String status             = (String)  row.getRaw(STATUS);
				String comments           = (String)  row.getRaw(COMMENTS);
				
				NodeProperty property = new NodePropertyImpl(type, description, startPosition, endPosition, residue, status, comments);
				properties.addProperty(property);
			}
		}
		return properties;
	}
	
	@Override
	public void deletePropertiesBySource(String source) {
		List<Integer> primaryKeys = new ArrayList<Integer>();
		Collection<CyRow> rows    = getTable().getMatchingRows(SOURCE, source);

		for (CyRow row : rows) {
			primaryKeys.add((Integer) row.getRaw(ID));
		}
		deleteRows(primaryKeys);		
	}
	
	@Override
	public List<?> getCompositeKey(Integer id) {
		String source         = getTable().getRow(id).get(SOURCE, String.class);
		String sharedNodeName = getTable().getRow(id).get(SHARED_NODE_NAME, String.class);
		String type           = getTable().getRow(id).get(TYPE, String.class);
		String description    = getTable().getRow(id).get(DESCRIPTION, String.class);
		int startPosition     = getTable().getRow(id).get(START_POSITION, Integer.class);
		int endPosition       = getTable().getRow(id).get(END_POSITION, Integer.class);
		String residue        = getTable().getRow(id).get(RESIDUE, String.class);
		String status         = getTable().getRow(id).get(STATUS, String.class);
		String comments       = getTable().getRow(id).get(COMMENTS, String.class);
		
		NodeProperty property = new NodePropertyImpl(type, description, startPosition, endPosition, residue, status, comments);
		return Arrays.asList(source, sharedNodeName, property);		
	}
	
	@Override
	public Set<String> getTypesBySource(String source) {
		Set<String> typeSet    = new HashSet<String>();
		Collection<CyRow> rows = getTable().getMatchingRows(SOURCE, source);

		for (CyRow row : rows) {
			String type = row.get(TYPE, String.class);
			typeSet.add(type);
		}
		return typeSet;
	}
	
	@Override
	public Set<String> getAllSources() {
		Set<String> sourceSet = new HashSet<String>(getTable().getColumn(SOURCE).getValues(String.class));
		return sourceSet;
	}
	
}