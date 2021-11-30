package org.cytoscape.PTMOracle.internal.util.tasks;

import org.cytoscape.PTMOracle.internal.util.CytoscapeServices;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.task.AbstractNetworkTask;

/**
 * Root network tasks are individual tasks that 
 * involve information from the root network.
 * @author aidan
 */
public abstract class AbstractRootNetworkTask extends AbstractNetworkTask {

	private CyRootNetwork rootNetwork;
	
	public AbstractRootNetworkTask(CyNetwork network) {
		super(network);
		
		setRootNetwork(network);
	}
	
	public CyRootNetwork getRootNetwork() {
		return rootNetwork;
	}
	
	public void setRootNetwork(CyNetwork network) {
		this.rootNetwork = CytoscapeServices.getRootNetwork(network);
	}

	public CyTable getRootNodeTable() {
		return rootNetwork.getSharedNodeTable();
	}
	
	public CyTable getRootEdgeTable() {
		return rootNetwork.getSharedEdgeTable();
	}
	
}
