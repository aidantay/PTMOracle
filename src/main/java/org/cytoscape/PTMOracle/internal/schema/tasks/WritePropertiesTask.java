package org.cytoscape.PTMOracle.internal.schema.tasks;

import java.io.File;

import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * Task for writing oracle properties to file. 
 * @author aidan
 */
public class WritePropertiesTask extends AbstractRootNetworkTask {

	@Tunable(description="Select file:", params="fileCategory=unspecified;input=false")
	public File file;
	
	public WritePropertiesTask(CyNetwork network) {
		super(network);
	}
	
	public File getFile() {
		return file;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Writing Oracle");
		taskMonitor.setStatusMessage("Writing properties in Oracle ");

		ExportPropertiesFromOracleTask task = new ExportPropertiesFromOracleTask(network, getFile());
		insertTasksAfterCurrentTask(task);
	}

}
