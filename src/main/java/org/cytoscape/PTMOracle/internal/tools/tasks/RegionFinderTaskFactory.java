package org.cytoscape.PTMOracle.internal.tools.tasks;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * Task factory for the RegionFinder OracleTool menu.
 * @author aidan
 */
public class RegionFinderTaskFactory extends AbstractNetworkTaskFactory {

	public RegionFinderTaskFactory() {
		super();
	}
	
	@Override
	public TaskIterator createTaskIterator(CyNetwork arg0) {
		return new TaskIterator(new RegionFinderTask(arg0));
	}

}
