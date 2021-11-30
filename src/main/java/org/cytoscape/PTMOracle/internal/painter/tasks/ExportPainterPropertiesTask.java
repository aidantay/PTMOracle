package org.cytoscape.PTMOracle.internal.painter.tasks;

import java.io.File;

import org.cytoscape.PTMOracle.internal.io.PainterWriter;
import org.cytoscape.PTMOracle.internal.io.write.PainterXMLWriterImpl;
import org.cytoscape.PTMOracle.internal.model.ColorScheme;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * Task for writing painter properties to file. 
 * @author aidan
 */
public class ExportPainterPropertiesTask extends AbstractTask {

	@Tunable(description="Select file:", params="fileCategory=unspecified;input=false")
	public File file;
	
	private ColorScheme colorScheme;
		
	public ExportPainterPropertiesTask(ColorScheme colorScheme) {
		super();
		
		this.colorScheme = colorScheme;
	}
	
	public File getFile() {
		return file;
	}
	
	public ColorScheme getColorScheme() {
		return colorScheme;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Writing OraclePainter");
		taskMonitor.setStatusMessage("Writing painter properties");
		
		PainterWriter writer = new PainterXMLWriterImpl(getFile(), getColorScheme()); 
		writer.run(taskMonitor);
	}

}
