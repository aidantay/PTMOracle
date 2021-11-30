package org.cytoscape.PTMOracle.internal.tools.tasks;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * Task factory for the PairFinder OracleTool menu.
 * @author aidan
 */
public class PairFinderTaskFactory extends AbstractNetworkTaskFactory {

	public PairFinderTaskFactory() {
		super();
	}
	
	@Override
	public TaskIterator createTaskIterator(CyNetwork arg0) {
		return new TaskIterator(new PairFinderTask(arg0));
	}

}
