package org.cytoscape.PTMOracle.internal.tools.tasks;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * Task factory for the Calculator OracleTool menu. 
 * @author aidan
 */
public class CalculatorTaskFactory extends AbstractNetworkTaskFactory {

	public CalculatorTaskFactory() {
		super();
	}
	
	@Override
	public TaskIterator createTaskIterator(CyNetwork arg0) {
		return new TaskIterator(new CalculatorTask(arg0));
	}

}
