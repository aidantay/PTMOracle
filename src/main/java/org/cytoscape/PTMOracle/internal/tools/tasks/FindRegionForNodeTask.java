package org.cytoscape.PTMOracle.internal.tools.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * Task for finding regions with PTMs
 * @author aidan
 */
public class FindRegionForNodeTask extends AbstractRootNetworkTask {

	private String regionType;
	private String region;
	private String ptm;
	private String targetResidues;

	public FindRegionForNodeTask(CyNetwork network, String regionType, String region, String ptm, String targetResidues) {
		super(network);

		this.regionType     = regionType;
		this.region         = region;
		this.ptm            = ptm;
		this.targetResidues = targetResidues;
	}
	
	public String getRegionType() {
		return regionType;
	}
	
	public String getRegion() {
		return region;
	}
	
	public String getPtm() {
		return ptm;
	}
	
	public String getTargetResidues() {
		return targetResidues;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		
		// Create a Cytoscape column that summarises the results 
		String columnName = createCytoscapeColumn();
			
		// Find all occurrences of the motif in each node 
		findRegion(columnName);
	}

	private String createCytoscapeColumn() {
		// Get the name of the column
		// Remove the previous column in the Cytoscape table
		// Create a new column in the Cytoscape table
		// Update the column in the Cytoscape table with counts
		String columnName = "NumberOf" + getRegion() + "With" + getPtm();
		if (!targetResidues.isEmpty()) {
			columnName = columnName + "On" + getTargetResidues();
		}
		
		getRootNodeTable().deleteColumn(columnName);
		getRootNodeTable().createColumn(columnName, Integer.class, false);
		
		return columnName;
	}
	
	private void findRegion(String columnName) {
		for (CyNode node : getRootNetwork().getNodeList()) {
			String sharedNodeName = (String) getRootNetwork().getSharedNodeTable().getRow(node.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);			
			PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(sharedNodeName, getRootNetwork().toString(), true);
			
			Set<Property> regionSet = (uniquePropertyMap.containsPropertyType(getRegionType())) ?
									   uniquePropertyMap.getPropertiesByType(getRegionType()) :
									   new HashSet<Property>();

			Set<Property> ptmSet = (uniquePropertyMap.containsPropertyType(PropertyTable.PTM)) ?
									uniquePropertyMap.getPropertiesByType(PropertyTable.PTM) :
									new HashSet<Property>();

			// Iterate through the list to create a new NodeProperty List
			List<NodeProperty> regionList = new ArrayList<NodeProperty>();
			for (Property p : regionSet) {
				if (p.getDescription().equals(getRegion())) {
					NodeProperty nodeProperty = (NodeProperty) p;
					regionList.add(nodeProperty);
				}
			}
			Collections.sort(regionList, new NodePropertyComparator());

			// Iterate through the list to create a new NodeProperty List
			List<NodeProperty> ptmList = new ArrayList<NodeProperty>();
			for (Property p : ptmSet) {
				if (p.getDescription().equals(getPtm())) {
					NodeProperty nodeProperty = (NodeProperty) p;
					ptmList.add(nodeProperty);
				}
			}
			Collections.sort(ptmList, new NodePropertyComparator());

			// Count the number of regions with PTMs	
			int numRegions = isPtmInRegion(regionList, ptmList);
			getRootNetwork().getSharedNodeTable().getRow(node.getSUID()).set(columnName, numRegions);
		}
		
	}

	private int isPtmInRegion(List<NodeProperty> regionList, List<NodeProperty> ptmList) {
		
		int numRegionsWithPtms = 0;
		
		for (NodeProperty region : regionList) {
			for (NodeProperty modification : ptmList) {
				if (modification.getDescription().equals(getPtm())) {
					int regionStart = region.getStartPosition();
					int regionEnd   = region.getEndPosition();

					int modificationPosition = modification.getStartPosition();
					String modificationResidue  = modification.getResidue();
					
					if (!getTargetResidues().isEmpty()) {
						Pattern p = Pattern.compile(getTargetResidues());
					   	Matcher matcher = p.matcher(modificationResidue);
					   	// If the modification does not occur on the target residue, then we move onto the next modification
					   	if (!matcher.find()) {
					   		continue;
					   	}
					}
					
					if (modificationPosition >= regionStart && modificationPosition <= regionEnd) {
						numRegionsWithPtms++;
						break;	// Move onto the next region if we have a PTM in the current region.
					}
				}
			}
		}
		
		return numRegionsWithPtms;
	}

}
