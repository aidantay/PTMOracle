package org.cytoscape.PTMOracle.internal.tools.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.model.util.NodePropertyComparator;
import org.cytoscape.PTMOracle.internal.schema.PropertyTable;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.work.TaskMonitor;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

/**
 * Task for finding PTMs separated by an AA range  
 * @author aidan
 */
public class FindPairForNodeTask extends AbstractRootNetworkTask {

	private String ptm1;
	private String ptm2;
	private int distance;
	private boolean cumulativeFlag;
	
	public FindPairForNodeTask(CyNetwork network, String ptm1, String ptm2, int distance, boolean cumulativeFlag) {
		super(network);
		
		this.ptm1 = ptm1;
		this.ptm2 = ptm2;
		this.distance = distance;
		this.cumulativeFlag = cumulativeFlag;
	}
	
	public String getPtm1() {
		return ptm1;
	}
	
	public String getPtm2() {
		return ptm2;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public boolean getCumulativeFlag() {
		return cumulativeFlag;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		// Create a Cytoscape column that summarises the results 
		String columnName = createCytoscapeColumn();
			
		// Find all occurrences of the motif in each node
		findPair(columnName);
	}
	
	private String createCytoscapeColumn() {
		// Get the name of the column
		// Remove the previous column in the Cytoscape table
		// Create a new column in the Cytoscape table
		// Update the column in the Cytoscape table with counts
		String columnName = "NumberOf" + getPtm1() + "And" + getPtm2() + "Within" + distance + "AAs";  
		if (getCumulativeFlag()) {
			columnName = "Cumulative" + columnName;
		}
		getRootNodeTable().deleteColumn(columnName);
		getRootNodeTable().createColumn(columnName, Integer.class, false);
		
		return columnName;
	}
	
	private void findPair(String columnName) {

		for (CyNode node : getRootNetwork().getNodeList()) {
			String sharedNodeName = (String) getRootNetwork().getSharedNodeTable().getRow(node.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);
			PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(sharedNodeName, getRootNetwork().toString(), true);
			
			Set<Property> propertySet = (uniquePropertyMap.containsPropertyType(PropertyTable.PTM)) ?
										uniquePropertyMap.getPropertiesByType(PropertyTable.PTM) :
										new HashSet<Property>();

			// Iterate through the list to create a new NodeProperty List
			List<NodeProperty> propertyList = new ArrayList<NodeProperty>();
			for (Property p : propertySet) {
				NodeProperty nodeProperty = (NodeProperty) p;
				propertyList.add(nodeProperty);
			}
			Collections.sort(propertyList, new NodePropertyComparator());
			
			int numPairs = 0;
			if (getCumulativeFlag()) {
				for (int distance = 0; distance <= getDistance(); distance++) {
					numPairs = numPairs + isPairWithinDistance(getPtm1(), getPtm2(), distance, propertyList);
				}
			} else {
				numPairs = isPairWithinDistance(getPtm1(), getPtm2(), getDistance(), propertyList);
			}
			
			getRootNetwork().getSharedNodeTable().getRow(node.getSUID()).set(columnName, numPairs);
		}		
		
	}
	
	private int isPairWithinDistance(String ptm1, String ptm2, int distance, List<NodeProperty> propertyList) {

		int numPairs = 0;

		// We are basically checking by brute force (all possibilities)
		// To do this, we process each property and all properties after it.
		for (int i = 0; i < propertyList.size(); i++) {
			
			// Separate the first property from the remainder of the propertyList
			NodeProperty property1 = propertyList.get(i);

			// If there are still properties after property1, then we will count for pairs
			// Basically, we continue until we reach the end
			if (i + 1 < propertyList.size()) {
				if (property1.getDescription().equals(ptm1)) {
					// We need a copy of the peak list so that we can do comparisons
					List<NodeProperty> parentPropertyList = new ArrayList<NodeProperty>(propertyList.subList(i + 1, propertyList.size()));

					numPairs = numPairs + countPairs(distance, ptm2, property1, parentPropertyList);
					
				} else if (property1.getDescription().equals(ptm2)) {
					// We need a copy of the peak list so that we can do comparisons
					List<NodeProperty> parentPropertyList = new ArrayList<NodeProperty>(propertyList.subList(i + 1, propertyList.size()));

					numPairs = numPairs + countPairs(distance, ptm1, property1, parentPropertyList);
				}
			}
		}
		return numPairs;
	}
	
	private int countPairs(int distance, String ptm, NodeProperty property1, List<NodeProperty> parentPropertyList) {
		
		if (parentPropertyList.isEmpty()) {
			return 0;
			
		} else {
			NodeProperty property2 = parentPropertyList.remove(0);
			int range = Math.abs(property1.getStartPosition() - property2.getStartPosition());
	
			if (range == distance) {
				if (property2.getDescription().equals(ptm)) {
					return countPairs(distance, ptm, property1, parentPropertyList) + 1;
				}
			}
		}
		return 0;
	}

}
