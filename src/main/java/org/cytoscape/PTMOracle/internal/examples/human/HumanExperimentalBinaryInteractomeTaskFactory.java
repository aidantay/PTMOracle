package org.cytoscape.PTMOracle.internal.examples.human;

import org.cytoscape.PTMOracle.internal.examples.AbstractExampleNetworkTaskFactory;
import org.cytoscape.PTMOracle.internal.examples.ExampleNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.Task;

/**
 * Task factory for creating the experimentally derived human PPI network.
 * @author aidan
 */
public class HumanExperimentalBinaryInteractomeTaskFactory extends AbstractExampleNetworkTaskFactory {

	public HumanExperimentalBinaryInteractomeTaskFactory(CyNetworkManager netMgr,
			CyNetworkNaming namingUtil, CyNetworkFactory cnf,
			CyNetworkViewManager viewMgr, CyNetworkViewFactory cnvf,
			VisualMappingManager visMapMgr) {

		super(netMgr, namingUtil, cnf, viewMgr, cnvf, visMapMgr);
	}
	
	public ExampleNetwork createExampleNetwork() {
		return new HumanExperimentalBinaryInteractome();
	}
	
	public Task createTask(ExampleNetwork exampleNetwork) {
		return new HumanExperimentalBinaryInteractomeTask(getNetMgr(), getNamingUtil(), getCnf(), getViewMgr(), getCnvf(), getVisMapMgr(), exampleNetwork);		
	}
}

