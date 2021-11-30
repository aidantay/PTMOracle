package org.cytoscape.PTMOracle.internal.schema.tasks;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * Task for reading oracle files. 
 * @author aidan
 */
public class WritePropertiesTaskFactory extends AbstractNetworkTaskFactory {

	public WritePropertiesTaskFactory() {
		super();
	}
	
	@Override
	public TaskIterator createTaskIterator(CyNetwork arg0) {
		return new TaskIterator(new WritePropertiesTask(arg0));
	}

}
