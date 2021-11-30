package org.cytoscape.PTMOracle.internal.schema.tasks;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * Task for reading oracle files. 
 * @author aidan
 */
public class ReadPropertiesTaskFactory extends AbstractNetworkTaskFactory {

	public ReadPropertiesTaskFactory() {
		super();
	}
	
	@Override
	public TaskIterator createTaskIterator(CyNetwork arg0) {
		return new TaskIterator(new ReadPropertiesTask(arg0));
	}

}
