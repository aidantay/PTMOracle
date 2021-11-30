package org.cytoscape.PTMOracle.internal.tools.tasks;

import java.util.List;
import java.util.Set;

import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.work.TaskMonitor;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

/**
 * Task for counting keywords
 * @author aidan
 */
public class CountKeywordTask extends AbstractRootNetworkTask {

	private String propertyType;
	private List<String> keywords;
	
	public CountKeywordTask(CyNetwork network, String propertyType, List<String> keywords) {
		super(network);
		
		this.propertyType = propertyType;
		this.keywords = keywords;
	}
	
	public String getPropertyType() {
		return propertyType;
	}
	
	public List<String> getKeywords() {
		return keywords;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		
		// Go through each keyword in the set
		for (String keyword : getKeywords()) {

			// Get the name of the column
			String columnName = "NumberOf" + keyword;
			boolean isNodeProperty = getOracle().getPropertyTable().getNodePropertyFlag(getPropertyType());
			
			// Remove the previous column in the Cytoscape table
			// Create a new column in the Cytoscape table
			// Update the column in the Cytoscape table with counts
			if (isNodeProperty) {
				getRootNodeTable().deleteColumn(columnName);
				getRootNodeTable().createColumn(columnName, Integer.class, false);
				countNodeKeywords(keyword, columnName);
				
			} else {
				getRootEdgeTable().deleteColumn(columnName);
				getRootEdgeTable().createColumn(columnName, Integer.class, false);
				countEdgeKeywords(keyword, columnName);
			}
		}
	}
		
	private void countNodeKeywords(String keyword, String columnName) {
		for (CyNode node : getRootNetwork().getNodeList()) {
			String sharedNodeName = (String) getRootNetwork().getSharedNodeTable().getRow(node.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);			
			PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(sharedNodeName, getRootNetwork().toString(), true);
			int numKeywords = countKeywords(uniquePropertyMap, keyword);
			getRootNetwork().getSharedNodeTable().getRow(node.getSUID()).set(columnName, numKeywords);
		}
	}
	
	private void countEdgeKeywords(String keyword, String columnName) {
		for (CyEdge edge : getRootNetwork().getEdgeList()) {
			String sharedEdgeName = (String) getRootNetwork().getSharedNodeTable().getRow(edge.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);			
			PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(sharedEdgeName, getRootNetwork().toString(), false);
			int numKeywords = countKeywords(uniquePropertyMap, keyword);
			getRootNetwork().getSharedEdgeTable().getRow(edge.getSUID()).set(columnName, numKeywords);
		}
	}

	private int countKeywords(PropertyMap uniquePropertyMap, String keyword) {
		int numKeywords = 0;
		
		if (uniquePropertyMap.containsPropertyType(getPropertyType())) {
			Set<Property> properties = uniquePropertyMap.getPropertiesByType(getPropertyType());
			for (Property p : properties) {
				if (p.getDescription().equals(keyword)) {
					numKeywords++;
				}
			}
		}
		
		return numKeywords;
	}
	

}
