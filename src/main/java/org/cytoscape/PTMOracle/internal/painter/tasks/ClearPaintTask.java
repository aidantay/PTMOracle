package org.cytoscape.PTMOracle.internal.painter.tasks;

import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.customgraphics.CyCustomGraphics;
import org.cytoscape.work.TaskMonitor;

import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentNetworkView;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentRenderingEngine;

/**
 * Task for clearing the painter style from the network
 * @author 
 */
public class ClearPaintTask extends AbstractRootNetworkTask {

	public ClearPaintTask(CyNetwork network) {
		super(network);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run(TaskMonitor taskMonitor) throws Exception {

		// Clear the paint only for nodes in the CURRENT network
		for (CyNode node : network.getNodeList()) {
			VisualLexicon lexicon = getCurrentRenderingEngine().getVisualLexicon();
			VisualProperty<CyCustomGraphics<?>> vp = (VisualProperty<CyCustomGraphics<?>>) lexicon.lookup(CyNode.class, "NODE_CUSTOMGRAPHICS_9");	// We need to use THIS lexicon for custom graphics
			
			View<CyNode> nodeView = getCurrentNetworkView().getNodeView(node);
			nodeView.clearValueLock(vp);
		}		
	}

}