package org.cytoscape.PTMOracle.internal.schema.tasks;

import java.io.File;

import org.cytoscape.PTMOracle.internal.io.PropertyWriter;
import org.cytoscape.PTMOracle.internal.io.write.PropertyTSVWriterImpl;
import org.cytoscape.PTMOracle.internal.schema.swing.ExportPropertiesPanel;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * Task for writing oracle properties to file. 
 * @author aidan
 */
public class ExportPropertiesFromOracleTask extends AbstractRootNetworkTask {

	@Tunable
	public ExportPropertiesPanel panel;
	
	private File outputFile;
	
	public ExportPropertiesFromOracleTask(CyNetwork network, File outputFile) {
		super(network);
		
		this.outputFile = outputFile;
		this.panel      = new ExportPropertiesPanel(network, outputFile);
	}

	@ProvidesTitle
	public String getMenuTitle() {
		return "Export";
	}
	
	public File getFile() {
		return outputFile;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Exporting properties in Oracle");
		
		PropertyWriter writer = new PropertyTSVWriterImpl(getFile(), panel.getTableDisplay()); 
		writer.run(taskMonitor);
	}
}
