package org.cytoscape.PTMOracle.internal.util.tasks;

import org.cytoscape.PTMOracle.internal.util.swing.TextDisplay;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * Individual task that involve updating information in a TextDisplay
 * We need a task for this formatting a TextPane 
 * @author aidan
 */
public class UpdateTextDisplayTask extends AbstractTask {

	private TextDisplay textDisplay;
	
	public UpdateTextDisplayTask(TextDisplay textDisplay) {
		super();
		
		setTextDisplay(textDisplay);
	}
	
	public TextDisplay getTextDisplay() {
		return textDisplay;
	}
	
	public void setTextDisplay(TextDisplay textDisplay) {
		this.textDisplay = textDisplay;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		getTextDisplay().updateTextDisplay();
	}
	
}
