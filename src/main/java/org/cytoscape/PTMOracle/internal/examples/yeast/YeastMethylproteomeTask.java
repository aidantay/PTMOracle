package org.cytoscape.PTMOracle.internal.examples.yeast;

import org.cytoscape.PTMOracle.internal.examples.AbstractExampleNetworkTask;
import org.cytoscape.PTMOracle.internal.examples.ExampleNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;

/**
 * Task for creating the yeast methyltransferase-substrate network.
 * @author aidan
 */
public class YeastMethylproteomeTask extends AbstractExampleNetworkTask {

	public YeastMethylproteomeTask(CyNetworkManager netMgr,
			CyNetworkNaming namingUtil, CyNetworkFactory cnf,
			CyNetworkViewManager viewMgr, CyNetworkViewFactory cnvf,
			VisualMappingManager visMapMgr, ExampleNetwork network) {
		
		super(netMgr, namingUtil, cnf, viewMgr, cnvf, visMapMgr, network);
	}

}
