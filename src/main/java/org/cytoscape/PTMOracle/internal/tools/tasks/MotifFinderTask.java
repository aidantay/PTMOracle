package org.cytoscape.PTMOracle.internal.tools.tasks;

import java.io.IOException;

import org.cytoscape.PTMOracle.internal.tools.swing.MotifFinderPanel;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.TunableValidator;

/**
 * Task for the MotifFinder OracleTool menu, which find motif sequences on proteins.
 * OracleTool for nodes only.
 * @author aidan
 */
public class MotifFinderTask extends AbstractRootNetworkTask implements TunableValidator {

	@Tunable
	public MotifFinderPanel panel;
	
	private String motifPattern;
	
	public MotifFinderTask(CyNetwork network) {
		super(network);
		
		panel = new MotifFinderPanel();
	}
	
	public MotifFinderPanel getPanel() {
		return panel;
	}

	@ProvidesTitle
	public String getMenuTitle() {
		return "MotifFinder";
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Finding motif sequences");
		taskMonitor.setStatusMessage("Finding motif sequences");

		FindMotifForNodeTask task = new FindMotifForNodeTask(network, motifPattern);
		insertTasksAfterCurrentTask(task);
	}
	
	@Override
	public ValidationState getValidationState(Appendable errMsg) {
		
		try {
			if (getPanel().getMotifField().length() == 0) {
				errMsg.append("Invalid parameters");
				return ValidationState.INVALID;
			}

			motifPattern     = getPanel().getMotifField();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ValidationState.OK;
	}

}
