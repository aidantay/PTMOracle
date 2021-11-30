package org.cytoscape.PTMOracle.internal.util.tasks;

import java.io.File;

import org.cytoscape.PTMOracle.internal.io.PropertyWriter;
import org.cytoscape.PTMOracle.internal.io.write.PropertyXMLWriterImpl;
import org.cytoscape.PTMOracle.internal.model.PropertyCollection;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * Task for writing oracle properties to file. 
 * @author aidan
 */
public class ExportConvertFileTask extends AbstractTask {

	@Tunable(description="Select output file:", params="fileCategory=unspecified;input=false")
	public File file;
	
	private PropertyCollection propertyCollection;
	
	public ExportConvertFileTask(PropertyCollection propertyCollection) {
		super();
		
		this.propertyCollection = propertyCollection;
	}

	public File getOutputFile() {
		return file;
	}
	
	public PropertyCollection getPropertyCollection() {
		return propertyCollection;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Exporting properties");

		PropertyWriter writer = new PropertyXMLWriterImpl(getOutputFile(), propertyCollection); 
		writer.run(taskMonitor);
	}
	
}
