package org.cytoscape.PTMOracle.internal.tools.tasks;

import java.io.IOException;

import org.cytoscape.PTMOracle.internal.tools.swing.PairFinderPanel;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.TunableValidator;

/**
 * Task for the PairFinder OracleTool menu, which finds PTMs separated by an AA range on proteins
 * OracleTool for nodes only
 * TODO Extend to multiple regions. Requires redesign of panel layout
 * TODO Extend to multiple PTMs. Requires redesign of panel layout
 * @author aidan
 */
public class PairFinderTask extends AbstractRootNetworkTask implements TunableValidator {

	@Tunable
	public PairFinderPanel panel;
	
	private String ptm1;
	private String ptm2;
	private int distance;
	private boolean cumulativeFlag;
	
	public PairFinderTask(CyNetwork network) {
		super(network);
		
		this.panel = new PairFinderPanel();
	}
	
	public PairFinderPanel getPanel() {
		return panel;
	}
	
	@ProvidesTitle
	public String getMenuTitle() {
		return "PairFinder";
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Finding PTM pairs");
		taskMonitor.setStatusMessage("Finding PTM pairs");

		FindPairForNodeTask task = new FindPairForNodeTask(network, ptm1, ptm2, distance, cumulativeFlag);
		insertTasksAfterCurrentTask(task);
	}

	@Override
	public ValidationState getValidationState(Appendable errMsg) {
		
		try {
			if (getPanel().getPtm1Field() == null || getPanel().getPtm2Field() == null || getPanel().getDistanceField() < 0) {
				errMsg.append("Invalid parameters");
				return ValidationState.INVALID;
			}

			ptm1           = getPanel().getPtm1Field();
			ptm2           = getPanel().getPtm2Field();
			distance       = getPanel().getDistanceField();
			cumulativeFlag = getPanel().getCumulativeField();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ValidationState.OK;
	}

}


