package org.cytoscape.PTMOracle.internal.preferences.tasks;

import java.io.IOException;

import org.cytoscape.PTMOracle.internal.painter.swing.OraclePainterPanel;
import org.cytoscape.PTMOracle.internal.preferences.swing.PreferencesPanel;
import org.cytoscape.PTMOracle.internal.schema.tasks.UpdateCytoscapeTask;
import org.cytoscape.PTMOracle.internal.util.CytoscapeServices;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.TunableValidator;

/**
 * Task for the Preferences menu. 
 * @author aidan
 */
public class PreferencesTask extends AbstractTask implements TunableValidator {

	@Tunable
	public PreferencesPanel panel;
		
	public PreferencesTask() {
		super();
		
		this.panel = new PreferencesPanel();
	}
	
	public PreferencesPanel getPanel() {
		return panel;
	}

	@ProvidesTitle
	public String getMenuTitle() {
		return "Preferences";
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Saving Oracle preferences");
		taskMonitor.setStatusMessage("Updating preferences in Oracle");
		
		for (TableDisplay table : getPanel().getTableDisplayList()) {
			table.updateOracle();
		}
		
		// Update the Cytoscape tables with Oracle properties
		UpdateCytoscapeTask updateCytoscapeTask = new UpdateCytoscapeTask();
		insertTasksAfterCurrentTask(updateCytoscapeTask);
		
		// Update OraclePainter
		updateOraclePainter();
	}
	
	private void updateOraclePainter() {
		OraclePainterPanel painterMenu = CytoscapeServices.getOraclePainterPanel();
		painterMenu.getModPainterPanel().clearColorPalette();
		painterMenu.getModPainterPanel().createColorPalette();
	}

	@Override
	public ValidationState getValidationState(Appendable errMsg) {
		try {
			errMsg.append("Source files may have to be re-imported after changes to the Property and/or Keyword tables.\n"
					      + "Would you like to continue?");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ValidationState.REQUEST_CONFIRMATION;
	}

}
