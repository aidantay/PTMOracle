package org.cytoscape.PTMOracle.internal.util.tasks;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * Task factory for the preferences menu. 
 * @author aidan
 */
public class ConvertFileTaskFactory extends AbstractTaskFactory {

	public ConvertFileTaskFactory() {
		super();
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new ConvertFileTask());
	}

}
