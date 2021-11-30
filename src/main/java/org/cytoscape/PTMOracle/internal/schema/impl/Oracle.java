package org.cytoscape.PTMOracle.internal.schema.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.model.property.NodePropertyImpl;
import org.cytoscape.PTMOracle.internal.model.property.PropertyMapImpl;
import org.cytoscape.PTMOracle.internal.schema.KeywordTable;
import org.cytoscape.PTMOracle.internal.schema.PropertyTable;
import org.cytoscape.PTMOracle.internal.schema.EdgePropertyTable;
import org.cytoscape.PTMOracle.internal.schema.NetworkMappingTable;
import org.cytoscape.PTMOracle.internal.schema.NodePropertyTable;
import org.cytoscape.PTMOracle.internal.schema.OracleTable;
import org.cytoscape.PTMOracle.internal.schema.impl.EdgePropertyTableImpl;
import org.cytoscape.PTMOracle.internal.schema.impl.KeywordTableImpl;
import org.cytoscape.PTMOracle.internal.schema.impl.NetworkMappingTableImpl;
import org.cytoscape.PTMOracle.internal.schema.impl.NodePropertyTableImpl;
import org.cytoscape.PTMOracle.internal.schema.impl.Oracle;
import org.cytoscape.PTMOracle.internal.schema.impl.PropertyTableImpl;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;

import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.PTM;
import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.SEQUENCE;

/**
 * Oracle is the back-end relational (partially?) database.
 * Contains all individual OracleTables. 
 * Oracle is global for ease of use.
 * TODO Handle for mergable edge properties
 * @author aidan
 */
public class Oracle {
	
	private static Oracle instance = new Oracle();
	
	private CyTableManager tableMgr;
	private CyTableFactory tableFactory;

	private PropertyTable propertyTable;
	private KeywordTable keywordTable;
	private NetworkMappingTable networkMappingTable;
	private NodePropertyTable nodePropertyTable;
	private EdgePropertyTable edgePropertyTable;
	
	private Oracle() {
		
	}
	
	public Oracle(CyTableManager tableMgr, CyTableFactory tableFactory) {
		getOracle().setTableManager(tableMgr);
		getOracle().setTableFactory(tableFactory);

//		getOracle().unregisterTablesInCytoscape();	// Placeholder. We use this line for testing purposes! Remove when necessary TODO
		getOracle().createTables(); // The following shouldn't actually do anything as it gets overriden later. BUT we need this so that other classes can work
		getOracle().registerTablesToCytoscape();
	}
	
	public static Oracle getOracle() {
		return instance;
	}

	public CyTableManager getTableManager() {
		return tableMgr;
	}
	
	public CyTableFactory getTableFactory() {
		return tableFactory;
	}

	public void setTableManager(CyTableManager tableMgr) {
		this.tableMgr = tableMgr;
	}
	
	public void setTableFactory(CyTableFactory tableFactory) {
		this.tableFactory = tableFactory;
	}
	
	public PropertyTable getPropertyTable() {
		return propertyTable;
	}
	
	public KeywordTable getKeywordTable() {
		return keywordTable;
	}
	
	public NetworkMappingTable getNetworkMappingTable() {
		return networkMappingTable;
	}

	public NodePropertyTable getNodePropertyTable() {
		return nodePropertyTable;
	}
	
	public EdgePropertyTable getEdgePropertyTable() {
		return edgePropertyTable;
	}

	public Set<OracleTable> getTables() {		
		Set<OracleTable> tableSet = new HashSet<OracleTable>();
		tableSet.add(getPropertyTable());
		tableSet.add(getKeywordTable());
		tableSet.add(getNetworkMappingTable());
		tableSet.add(getNodePropertyTable());
		tableSet.add(getEdgePropertyTable());
		return tableSet;
	}
	
	@SuppressWarnings("rawtypes")
	public PropertyMap getUniqueProperties(String sharedName, String rootNetworkName, boolean isNode) {

		PropertyMap uniquePropertyMap = new PropertyMapImpl();
		
		// Get all properties for a given node or edge
		for (String source : getNetworkMappingTable().getSources(rootNetworkName)) {
			PropertyMap propertyMap;
			if (isNode) {
				propertyMap = getNodePropertyTable().getProperties(Arrays.asList(source, sharedName));
				
			} else {
				propertyMap = getEdgePropertyTable().getProperties(Arrays.asList(source, sharedName));
			}

			for (String type : propertyMap.getAllPropertyTypes()) {
				for (Property property : propertyMap.getPropertiesByType(type)) {
					// If we have a sequence type, then we include it regardless of what the description is
					if (type.equals(SEQUENCE)) {
						uniquePropertyMap.addProperty(property);
					
					// Otherwise, only include properties for which there is a (keyword, type) pair 
					} else {
						List compositeKey = Arrays.asList(property.getDescription(), type);
						if (getKeywordTable().hasRow(compositeKey)) {
							uniquePropertyMap.addProperty(property);
						}
					}
				}
			}
		}

		// Adjust the properties for a given node or edge if necessary
		makeAdjustmentsToProperties(uniquePropertyMap);
		return uniquePropertyMap;
	}
	
	private void makeAdjustmentsToProperties(PropertyMap uniquePropertyMap) {
		
		for (String type : uniquePropertyMap.getAllPropertyTypes()) {
			// Merge any 'interval' properties that are overlapping
			// We will only do this for mergeable node properties
			if (getPropertyTable().getNodePropertyFlag(type) && getPropertyTable().getMergeableFlag(type)) {
				Set<Property> propertySet = uniquePropertyMap.getPropertiesByType(type);
				propertySet = mergeProperties(propertySet);
				uniquePropertyMap.addPropertySet(type, propertySet);
				
			// Update PTM properties with residue information from the protein sequence (IF we have it)
			} else if (type.equals(PTM)) {
				if (uniquePropertyMap.hasSequence()) {
					NodeProperty sequenceProperty = (NodeProperty) uniquePropertyMap.getSequence();
					updateProperties(uniquePropertyMap, type, sequenceProperty.getDescription());
				}
			}
		}
	}

	private Set<Property> mergeProperties(Set<Property> parentSet) {
		
		Set<Property> mergedPropertySet = new HashSet<Property>();
		boolean mergeComplete = true;

		for (Property parentProperty : parentSet) {
			NodeProperty nodeParentProperty = (NodeProperty) parentProperty;
			
			if (mergedPropertySet.isEmpty()) {
				mergedPropertySet.add(nodeParentProperty);
				
			} else {
				boolean isOverlap = false;
				for (Property mergedProperty : mergedPropertySet) {
					NodeProperty nodeMergedProperty = (NodeProperty) mergedProperty;
					
					// Check that mergeable properties are the same (description-wise)
					if (nodeParentProperty.getDescription().equals(nodeMergedProperty.getDescription())) {

						// Check that mergeable properties either overlap OR occur right next to each oher
						// If they do, then merge properties into single property					
						if (nodeMergedProperty.getStartPosition() - 1 <= nodeParentProperty.getEndPosition() &&
							nodeParentProperty.getStartPosition() <= nodeParentProperty.getEndPosition() + 1) {
							
							nodeMergedProperty.setStartPosition(Math.min(nodeParentProperty.getStartPosition(), nodeMergedProperty.getStartPosition()));
							nodeMergedProperty.setEndPosition(Math.max(nodeParentProperty.getEndPosition(), nodeMergedProperty.getEndPosition()));
							isOverlap = true;
							mergeComplete = false;
						}
						
					}
				}
				if (!isOverlap) {
					mergedPropertySet.add(nodeParentProperty);
				}
			}
		}
		
		// If we made a merge, then we need to recursively merge properties
		// So that all properties merged as much as possible 
		if (!mergeComplete) {
			mergedPropertySet = mergeProperties(mergedPropertySet);
		}

		return mergedPropertySet;
	}

	public void updateProperties(PropertyMap uniquePropertyMap, String type, String sequence) {
		
		Set<Property> propertySet = uniquePropertyMap.getPropertiesByType(type);
		for (Property p : propertySet) {
			NodeProperty nodeProperty = (NodeProperty) p;
			
			int position   = nodeProperty.getStartPosition();
			String residue = nodeProperty.getResidue();
			String status  = null;

			if (position < 0 || position > sequence.length()) {
				status = "Inconsistent with sequence - X";
				
			} else {
				String sequenceResidue = sequence.substring(position - 1, position);
				if (!residue.equals(sequenceResidue)) {
					status = "Inconsistent with sequence - " + sequenceResidue;
				}
			}
			
			if (status != null) {
				NodeProperty newNodeProperty = new NodePropertyImpl(nodeProperty.getType(), nodeProperty.getDescription(),
																	nodeProperty.getStartPosition(), nodeProperty.getEndPosition(), 
																	nodeProperty.getResidue(), status, nodeProperty.getComments());
				uniquePropertyMap.addProperty(newNodeProperty);
			}
		}
		
	}
	
	public void createTables() {
		propertyTable       = new PropertyTableImpl(tableFactory);
		keywordTable        = new KeywordTableImpl(tableFactory);
		networkMappingTable = new NetworkMappingTableImpl(tableFactory);
		nodePropertyTable   = new NodePropertyTableImpl(tableFactory);
		edgePropertyTable   = new EdgePropertyTableImpl(tableFactory);
	}
	
	public void registerTablesToCytoscape() {
		registerTable(getPropertyTable());
		registerTable(getKeywordTable());
		registerTable(getNetworkMappingTable());
		registerTable(getNodePropertyTable());
		registerTable(getEdgePropertyTable());
	}

	public void unregisterTablesInCytoscape() {
		unregisterTable(getPropertyTable());
		unregisterTable(getKeywordTable());
		unregisterTable(getNetworkMappingTable());
		unregisterTable(getNodePropertyTable());
		unregisterTable(getEdgePropertyTable());
	}
	
	private void registerTable(OracleTable oracleTable) {
		if (oracleTable != null) {
			CyTable registeredTable = getTableManager().getTable(oracleTable.getTable().getSUID());
			if (registeredTable == null) {
				getTableManager().addTable(oracleTable.getTable());
			}
		}
	}
		
	private void unregisterTable(OracleTable oracleTable) {
		if (oracleTable != null) {
			// We cannot unregister a table using the SUIDs
			// Instead, we just use the table name.
			for (CyTable registeredTable : getTableManager().getAllTables(true)) {
				if (registeredTable.getTitle().equals(oracleTable.getTableName())) {
					getTableManager().deleteTable(registeredTable.getSUID());
					break;
				}
			}
		}
	}

}
