package org.cytoscape.PTMOracle.internal.util.tasks;

import java.io.File;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * Task for the preferences menu. 
 * @author aidan
 */
public class ConvertFileTask extends AbstractTask {

	@Tunable(description="Select file to convert:", params="fileCategory=unspecified;input=true")
	public File file;
	
	public ConvertFileTask() {
		super();
	}
	
	public File getInputFile() {
		return file;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Converting file");

		ImportConvertFileTask task = new ImportConvertFileTask(getInputFile());
		insertTasksAfterCurrentTask(task);
	}

}
