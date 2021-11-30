package org.cytoscape.PTMOracle.internal.tools.tasks;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * Task factory for the MotifFinder OracleTool menu.
 * @author aidan
 */
public class MotifFinderTaskFactory extends AbstractNetworkTaskFactory {

	public MotifFinderTaskFactory() {
		super();
	}
	
	@Override
	public TaskIterator createTaskIterator(CyNetwork arg0) {
		return new TaskIterator(new MotifFinderTask(arg0));
	}

}
