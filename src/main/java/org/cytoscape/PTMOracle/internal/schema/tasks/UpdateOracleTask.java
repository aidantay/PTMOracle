package org.cytoscape.PTMOracle.internal.schema.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyCollection;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.model.property.PropertyMapImpl;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.work.TaskMonitor;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

/**
 * Task for updating oracle tables
 * TODO Improve method for edges
 * @author aidan
 */
public class UpdateOracleTask extends AbstractRootNetworkTask {

	private String sourceString;
	private PropertyCollection resolvedPropertyCollection;
	private String mappingColumn;
	
	public UpdateOracleTask(CyNetwork network, PropertyCollection resolvedPropertyCollection, 
			String sourceString, String mappingColumn) {
		
		super(network);
		
		this.sourceString = sourceString;
		this.resolvedPropertyCollection = resolvedPropertyCollection;
		this.mappingColumn = mappingColumn;
	}
	
	public String getSourceString() {
		return sourceString;
	}
	
	public PropertyCollection getResolvedPropertyCollection() {
		return resolvedPropertyCollection;
	}
	
	public String getMappingColumn() {
		return mappingColumn;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Importing properties into Oracle");

		// Delete properties of the same source from property tables (NODE && EDGE)
		// Importing the same entries will OVERWRITE the previous ones
		removePrevProperties();
		
		// Add all properties into the Oracle (NODE && EDGE)
		addCurrProperties();
	}

	private void removePrevProperties() {
		// We do not need to remove the previous network mapping 
		// Would be pointless if we just add it in again anyway
		// Instead just check for the mapping before we insert a new mapping
		// Different networks with the same source mappings should be updated accordingly anyway
		
		// Remove previous NODE && EDGE properties in Oracle
		getOracle().getNodePropertyTable().deletePropertiesBySource(getSourceString());
		getOracle().getEdgePropertyTable().deletePropertiesBySource(getSourceString());
	}
	
	private void addCurrProperties() {
		List<String> networkMapPrimaryKey = Arrays.asList(getRootNetwork().toString(), getSourceString());
		
		// Insert network-source mapping into network mapping table if we do not have one
		if (!getOracle().getNetworkMappingTable().hasRow(networkMapPrimaryKey)) {
			getOracle().getNetworkMappingTable().insertRow(networkMapPrimaryKey);			
		}

		// Determine whether the column is a list or not
		boolean isList = getRootNodeTable().getColumn(getMappingColumn()).getType().equals(List.class);

		// Add current NODE && EDGE properties to Oracle
		addNodeProperties(isList);
		addEdgeProperties(isList);
	}
	
	private void addNodeProperties(boolean isList) {
		// Process each node and add all possible properties to the Oracle
		for (CyNode node : getRootNetwork().getNodeList()) {
			List<Object> idList = getIdList(node, isList);
			String sharedNodeName = (String) getRootNodeTable().getRow(node.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);
			if (idList != null) {
				PropertyMap propertyMap = new PropertyMapImpl();
				for (Object o : idList) {
					String id = (String) String.valueOf(o);
					if (getResolvedPropertyCollection().containsId(id)) {
						propertyMap.putAll(getResolvedPropertyCollection().getPropertyMap(id));	
					}
				}
				addPropertyResult(sharedNodeName, propertyMap);
			}
		}
	}
	
	private void addEdgeProperties(boolean isList) {
		// Process each edge and add all possible properties to the Oracle
		for (CyEdge edge : getRootNetwork().getEdgeList()) {
			CyNode sourceNode = edge.getSource();
			CyNode targetNode = edge.getTarget();
			
			List<Object> sourceIdList = getIdList(sourceNode, isList);
			List<Object> targetIdList = getIdList(targetNode, isList);
			
			String sharedEdgeName = (String) getRootEdgeTable().getRow(edge.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);

			if (sourceIdList != null && targetIdList != null) {
				for (Object o1 : sourceIdList) {
					for (Object o2 : targetIdList) {
						String sourceId = (String) String.valueOf(o1);
						String targetId = (String) String.valueOf(o2);

						// Since we are not dealing with directed edges, we look at both
						String A_B = sourceId + getResolvedPropertyCollection().getDelimiter() + targetId;
						String B_A = targetId + getResolvedPropertyCollection().getDelimiter() + sourceId;
						
						if (getResolvedPropertyCollection().containsId(A_B)) {
							PropertyMap propertyMap = getResolvedPropertyCollection().getPropertyMap(A_B);
							addPropertyResult(sharedEdgeName, propertyMap);
							
						} else if (getResolvedPropertyCollection().containsId(B_A)) {
							PropertyMap propertyMap = getResolvedPropertyCollection().getPropertyMap(B_A);
							addPropertyResult(sharedEdgeName, propertyMap);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Object> getIdList(CyNode node, boolean isList) {
		List<Object> idList = null;
		if (isList) {
			idList = (List<Object>) getRootNodeTable().getRow(node.getSUID()).getRaw(getMappingColumn());
		} else {
			Object id = getRootNodeTable().getRow(node.getSUID()).getRaw(getMappingColumn());
			idList = new ArrayList<Object>();
			idList.add(id);
		}
		return idList;
	}

	private void addPropertyResult(String componentName, PropertyMap propertyMap) {
		// Add in the properties if we have them
		if (propertyMap.hasProperties()) {
			for (String propertyType : propertyMap.getAllPropertyTypes()) {
				for (Property property : propertyMap.getPropertiesByType(propertyType)) {
					List<?> propertyRow = Arrays.asList(getSourceString(), componentName, property);
					if (getOracle().getPropertyTable().getNodePropertyFlag(propertyType)) {
						getOracle().getNodePropertyTable().insertRow(propertyRow);
					} else {
						getOracle().getEdgePropertyTable().insertRow(propertyRow);
					}
				}
			}
		}
	}
}
