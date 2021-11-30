package org.cytoscape.PTMOracle.internal.schema.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.cytoscape.PTMOracle.internal.schema.AbstractOracleTable;
import org.cytoscape.PTMOracle.internal.schema.NetworkMappingTable;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;

/**
 * Table of network-source pairings.
 * These pairings map the root network to the imported oracle files.
 * Table stores: 
 *  - ID (Primary Key)
 *  - Name of the root network (Composite Key)
 *  - Name of the file source (Composite Key)
 * @author aidan
 */
public class NetworkMappingTableImpl extends AbstractOracleTable implements NetworkMappingTable {

	public NetworkMappingTableImpl(CyTableFactory tableFactory) {
		CyTable table = tableFactory.createTable(getTableName(), ID, Integer.class, false, true);
		table.createColumn(ROOT_NETWORK_NAME, String.class, false);
		table.createColumn(SOURCE, String.class, false);
		setTable(table);
 		setColumnNames(Arrays.asList(ID, ROOT_NETWORK_NAME, SOURCE));
	}
	
	@Override
	public String getTableName() {
		return "NetworkMappings";
	}

 	@Override
	public void insertRow(List<?> rowValues) {
		if (rowValues.size() + 1 != getTable().getColumns().size()) {
			throw new IllegalArgumentException("Unable to insert row. Invalid number of columns\t" + getTableName());
		}

		String rootNetworkName = (String) rowValues.get(0);
		String source          = (String) rowValues.get(1);
		int id = getLastRowIndex() + 1;

		if (hasRow(Arrays.asList(rootNetworkName, source))) {
			throw new KeyAlreadyExistsException("Unable to insert row. Primary key exists\t" + getTableName());
		}

		CyRow row = getTable().getRow(id);
		row.set(ROOT_NETWORK_NAME, rootNetworkName);
		row.set(SOURCE, source);
	}
	
	@Override
	public boolean hasRow(Object primaryKey) {
		List<?> primaryKeys = (List<?>) primaryKey;
		String rootNetworkName = (String) primaryKeys.get(0);
		String source          = (String) primaryKeys.get(1);
		
		Collection<CyRow> rows = getTable().getMatchingRows(ROOT_NETWORK_NAME, rootNetworkName);
		for (CyRow row : rows) {
			if (row.get(SOURCE, String.class).equals(source)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public CyRow getRow(Object primaryKey) {
		List<?> primaryKeys = (List<?>) primaryKey;
		String rootNetworkName = (String) primaryKeys.get(0);
		String source          = (String) primaryKeys.get(1);

		Collection<CyRow> rows = getTable().getMatchingRows(ROOT_NETWORK_NAME, rootNetworkName);
		for (CyRow row : rows) {
			if (row.get(SOURCE, String.class).equals(source)) {
				return row;
			}
		}
		return null;
	}
	
	@Override
	public List<?> getCompositeKey(Integer id) {
		String rootNetworkName = getTable().getRow(id).get(ROOT_NETWORK_NAME, String.class);
		String source          = getTable().getRow(id).get(SOURCE, String.class);
		return Arrays.asList(rootNetworkName, source);		
	}
	
	@Override
	public Set<String> getSources(String rootNetworkName) {
		Set<String> sourceSet = new HashSet<String>();
		Collection<CyRow> rows = getTable().getMatchingRows(ROOT_NETWORK_NAME, rootNetworkName);
		for (CyRow row : rows) {
			String source = (String) row.getRaw(SOURCE);
			sourceSet.add(source);
		}
		return sourceSet;
	}
	
	@Override
	public Set<String> getRootNetworkNames(String source) {
		Set<String> rootNetworkNameSet= new HashSet<String>();
		Collection<CyRow> rows = getTable().getMatchingRows(SOURCE, source);
		for (CyRow row : rows) {
			String rootNetworkName = (String) row.getRaw(ROOT_NETWORK_NAME);
			rootNetworkNameSet.add(rootNetworkName);
		}
		return rootNetworkNameSet;
	}

}