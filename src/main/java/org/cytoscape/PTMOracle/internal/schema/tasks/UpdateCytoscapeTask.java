package org.cytoscape.PTMOracle.internal.schema.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getNetworkManager;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getRootNetwork;;

/**
 * Task for update Cytoscape tables
 * @author aidan
 */
public class UpdateCytoscapeTask extends AbstractTask {

	public UpdateCytoscapeTask() {
		super();
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Updating tables in Cytoscape");

		// Go through and update every network (root network) we got
		// This will account for when different networks have properties from the same source
		for (CyNetwork network : getNetworkManager().getNetworkSet()) {
			CyRootNetwork rootNetwork = getRootNetwork(network);
			Set<String> sources = getOracle().getNetworkMappingTable().getSources(rootNetwork.toString());

			Set<String> propertyTypes = new HashSet<String>();
			for (String source : sources) {
				propertyTypes.addAll(getOracle().getNodePropertyTable().getTypesBySource(source));
				propertyTypes.addAll(getOracle().getEdgePropertyTable().getTypesBySource(source));
			}

			// Remove previous columns in the Cytoscape table
			removePrevCytoscapeTableColumns(rootNetwork);
			
			// Create current columns in the Cytoscape table
			createCurrCytoscapeTableColumns(rootNetwork, propertyTypes);
			
			// Update columns in the Cytoscape table with properties from Oracle
			addPropertiesToCytoscapeTable(rootNetwork);
		}
	}
	
	private void removePrevCytoscapeTableColumns(CyRootNetwork rootNetwork) {
		for (Object primaryKey : getOracle().getPropertyTable().getPrimaryKeys()) {
			String type = (String) primaryKey;
			
			boolean isNodeProperty = getOracle().getPropertyTable().getNodePropertyFlag(type);
			String columnName      = getOracle().getPropertyTable().getPropertyColumnName(type);
			
			if (isNodeProperty) {
				rootNetwork.getSharedNodeTable().deleteColumn(columnName);
			} else {
				rootNetwork.getSharedEdgeTable().deleteColumn(columnName);
			}
		}
	}
	
	private void createCurrCytoscapeTableColumns(CyRootNetwork rootNetwork, Set<String> propertyTypes) {
		// Add columns for NODE && EDGE properties
		for (String type : propertyTypes) {
			boolean isNodeProperty = getOracle().getPropertyTable().getNodePropertyFlag(type);
			String columnName      = getOracle().getPropertyTable().getPropertyColumnName(type);
			boolean isList         = getOracle().getPropertyTable().getListFlag(type);

			if (isNodeProperty) {
				createColumns(rootNetwork.getSharedNodeTable(), columnName, isList);
			} else {
				createColumns(rootNetwork.getSharedEdgeTable(), columnName, isList);
			}
		}
	}
	
	private void createColumns(CyTable componentTable, String columnName, boolean isList) {
		// If the PropertyType requires a list, 
		// * Then assume that a column of type String is needed
		// * Otherwise assume a column of type Boolean is needed
		if (isList) {
			componentTable.createListColumn(columnName, String.class, false);
		} else {
			componentTable.createColumn(columnName, Boolean.class, false, false);
		}
	}

	private void addPropertiesToCytoscapeTable(CyRootNetwork rootNetwork) {
		
		CyTable rootNodeTable = rootNetwork.getSharedNodeTable();
		CyTable rootEdgeTable = rootNetwork.getSharedEdgeTable();
		
		// Add node properties to Cytoscape table
		for (CyNode node : rootNetwork.getNodeList()) {
			String sharedNodeName = (String) rootNodeTable.getRow(node.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);
			PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(sharedNodeName, rootNetwork.toString(), true);
			addProperties(uniquePropertyMap, rootNodeTable, node);
		}

		// Add edge properties to Cytoscape table
		for (CyEdge edge : rootNetwork.getEdgeList()) {
			String sharedEdgeName = (String) rootEdgeTable.getRow(edge.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);
			PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(sharedEdgeName, rootNetwork.toString(), false);
			addProperties(uniquePropertyMap, rootEdgeTable, edge);
		}
	}

	private void addProperties(PropertyMap uniquePropertyMap, CyTable componentTable, CyIdentifiable component) {
		for (String type : uniquePropertyMap.getAllPropertyTypes()) {
			String columnName = getOracle().getPropertyTable().getPropertyColumnName(type);
			boolean isList    = getOracle().getPropertyTable().getListFlag(type);

			if (isList) {
				Set<String> uniqueDescriptions = new HashSet<String>();
	 			for (Property property : uniquePropertyMap.getPropertiesByType(type)) {
			 		uniqueDescriptions.add(property.getDescription());
	 			}
				List<String> descriptionList = new ArrayList<String>(uniqueDescriptions);
		 		Collections.sort(descriptionList);
				componentTable.getRow(component.getSUID()).set(columnName, descriptionList);
			
			} else {
				if (!uniquePropertyMap.getPropertiesByType(type).isEmpty()) {
					componentTable.getRow(component.getSUID()).set(columnName, true);
		 		}
			}
		}
	}

}
