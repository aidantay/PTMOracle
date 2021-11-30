package org.cytoscape.PTMOracle.internal.schema.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.cytoscape.PTMOracle.internal.io.PropertyReader;
import org.cytoscape.PTMOracle.internal.io.read.PropertyTSVReaderImpl;
import org.cytoscape.PTMOracle.internal.io.read.PropertyXMLReaderImpl;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * Task for reading oracle files. 
 * @author aidan
 */
public class ReadPropertiesTask extends AbstractRootNetworkTask {

	@Tunable(description="Select file:", params="fileCategory=unspecified;input=true")
	public File file;
	
	public ReadPropertiesTask(CyNetwork network) {
		super(network);
	}
	
	public File getFile() {
		return file;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Reading Oracle file");
		taskMonitor.setStatusMessage("Reading properties in Oracle file");

		PropertyReader reader = null;
		
		// Check whether the input file is an XML or TSV
		// If it is neither, then an error SHOULD come up during the parsing stages
		if (isXmlFile()) {
			reader = new PropertyXMLReaderImpl(getFile());
		
		} else {
			reader = new PropertyTSVReaderImpl(getFile());
		}
		reader.run(taskMonitor);
		reader.convertDescriptionsToKeyword();
		
		ImportPropertiesIntoOracleTask task = new ImportPropertiesIntoOracleTask(network, reader);
		insertTasksAfterCurrentTask(task);
	}
	
	private boolean isXmlFile() throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader(getFile()));
		String line = fileReader.readLine();
		fileReader.close();
		
		if (line.split("\\t").length == 1) {
			return true;
		}
		
		return false;
	}

}
