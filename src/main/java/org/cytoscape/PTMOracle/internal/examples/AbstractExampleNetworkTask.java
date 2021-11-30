package org.cytoscape.PTMOracle.internal.examples;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * Abstract implementation of the task used to create an ExampleNetwork.
 * @author aidan
 */
public abstract class AbstractExampleNetworkTask extends AbstractTask {

	private final CyNetworkManager netMgr;
	private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil;
	private final CyNetworkViewManager viewMgr;
	private final CyNetworkViewFactory cnvf;
	private final VisualMappingManager visMapMgr;
	
	private final ExampleNetwork exampleNetwork;
	
	public AbstractExampleNetworkTask(CyNetworkManager netMgr, 
			CyNetworkNaming namingUtil, CyNetworkFactory cnf, 
			CyNetworkViewManager viewMgr, CyNetworkViewFactory cnvf,
			VisualMappingManager visMapMgr, ExampleNetwork exampleNetwork) {

		this.netMgr = netMgr;
		this.namingUtil = namingUtil;
		this.cnf = cnf;
		this.viewMgr = viewMgr;
		this.cnvf = cnvf;
		this.visMapMgr = visMapMgr;
		this.exampleNetwork = exampleNetwork;		
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Constructing network");
		taskMonitor.setStatusMessage("Creating " + exampleNetwork.getNetworkName() + " Network");

		// Create network
		CyNetwork network = cnf.createNetwork();

		// Set name for network
		network.getRow(network).set(CyNetwork.NAME, namingUtil.getSuggestedNetworkTitle(exampleNetwork.getNetworkName()));

		exampleNetwork.setNetwork(network);
		
		// Add the columns into the table for the network we want
		exampleNetwork.createColumns();

		// Parse the node and edge files to produce the network
		exampleNetwork.parseNetwork();
			
		// Add the network to the network manager
		netMgr.addNetwork(network);
	
		// Create network view and apply the current style
		CyNetworkView interactionView = cnvf.createNetworkView(network);
		exampleNetwork.applyLayout(interactionView);
		visMapMgr.getCurrentVisualStyle().apply(interactionView);
		interactionView.updateView();

		// Add the network view
		viewMgr.addNetworkView(interactionView);

	}
}
