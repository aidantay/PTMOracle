package org.cytoscape.PTMOracle.internal.examples.human;

import org.cytoscape.PTMOracle.internal.examples.AbstractExampleNetworkTask;
import org.cytoscape.PTMOracle.internal.examples.ExampleNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;

/**
 * Task for creating the experimentally derived human PPI network.
 * @author aidan
 */
public class HumanExperimentalBinaryInteractomeTask extends AbstractExampleNetworkTask {

	public HumanExperimentalBinaryInteractomeTask(CyNetworkManager netMgr,
			CyNetworkNaming namingUtil, CyNetworkFactory cnf,
			CyNetworkViewManager viewMgr, CyNetworkViewFactory cnvf,
			VisualMappingManager visMapMgr, ExampleNetwork network) {
		
		super(netMgr, namingUtil, cnf, viewMgr, cnvf, visMapMgr, network);
	}

}
