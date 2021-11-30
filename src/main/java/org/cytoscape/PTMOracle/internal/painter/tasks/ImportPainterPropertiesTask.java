package org.cytoscape.PTMOracle.internal.painter.tasks;

import java.awt.Color;
import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.io.PainterReader;
import org.cytoscape.PTMOracle.internal.io.read.PainterXMLReaderImpl;
import org.cytoscape.PTMOracle.internal.painter.swing.ModPainterPanel;
import org.cytoscape.PTMOracle.internal.painter.swing.MultiPainterPanel;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * Task for reader painter properties file.
 * @author aidan
 */
public class ImportPainterPropertiesTask extends AbstractTask {

	@Tunable(description="Select file:", params="fileCategory=unspecified;input=true")
	public File file;
	
	private ModPainterPanel modPainterPanel;
	private MultiPainterPanel multiPainterPanel;

	public ImportPainterPropertiesTask(ModPainterPanel modPainterPanel, MultiPainterPanel multiPainterPanel) {
		super();
		
		this.modPainterPanel = modPainterPanel;
		this.multiPainterPanel = multiPainterPanel;
	}
	
	public File getFile() {
		return file;
	}
	
	public ModPainterPanel getModPainterPanel() {
		return modPainterPanel;
	}
	
	public MultiPainterPanel getMultiPainterPanel() {
		return multiPainterPanel;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Reading OraclePainter file");
		taskMonitor.setStatusMessage("Reading painter properties");
		
		PainterReader reader = new PainterXMLReaderImpl(getFile());
		reader.run(taskMonitor);
		importColorScheme(reader.getColorScheme());
	}
	
	private void importColorScheme(Map<Pair<String, String>, Color> colorScheme) {
		
		boolean clearedPainter = false;
		
		for (Pair<String, String> pair : colorScheme.keySet()) {
			String attribute = pair.getLeft();
			String value     = pair.getRight();
			Color color      = colorScheme.get(pair);
			
	 		// Depending on whether we are using Multi or Mod painter
	 		// Attribute-value pairs means we are using MultiPainter
			if (attribute.isEmpty()) {
//				getModPainterPanel().importColor(value, color);
				
			} else {
				if (!clearedPainter) {
		    		getMultiPainterPanel().removeAllComponents();
		    		clearedPainter = true;
				}
				getMultiPainterPanel().importColor(attribute, value, color);
			}
		}
	}

}
