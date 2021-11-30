package org.cytoscape.PTMOracle.internal.tools.tasks;

import java.io.IOException;

import org.cytoscape.PTMOracle.internal.tools.swing.RegionFinderPanel;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.TunableValidator;

/**
 * Task for the RegionFinder OracleTool menu, which finds regions with PTMs on proteins
 * OracleTool for nodes only
 * TODO Extend to multiple regions. Requires redesign of panel layout 
 * TODO Extend to multiple PTMs. Requires redesign of panel layout
 * @author aidan
 */
public class RegionFinderTask extends AbstractRootNetworkTask implements TunableValidator {

	@Tunable
	public RegionFinderPanel panel;
	
	private String regionType;
	private String region;
	private String ptm;
	private String targetResidues;
	
	public RegionFinderTask(CyNetwork network) {
		super(network);

		this.panel = new RegionFinderPanel();
	}
	
	public RegionFinderPanel getPanel() {
		return panel;
	}
	
	@ProvidesTitle
	public String getMenuTitle() {
		return "RegionFinder";
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Finding regions with PTMs");
		taskMonitor.setStatusMessage("Finding regions with PTMs");
		
		FindRegionForNodeTask task = new FindRegionForNodeTask(network, regionType, region, ptm, targetResidues);
		insertTasksAfterCurrentTask(task);
	}
	
	@Override
	public ValidationState getValidationState(Appendable errMsg) {

		try {
			if (getPanel().getRegionField() == null || getPanel().getPtmField() == null) {
				errMsg.append("Invalid parameters");
				return ValidationState.INVALID;
			}
			
			if (!getPanel().getTargetResiduesField().isEmpty() 
				&& (!getPanel().getTargetResiduesField().startsWith("[") || !getPanel().getTargetResiduesField().endsWith("]"))) {
				errMsg.append("Invalid parameters");
				return ValidationState.INVALID;
			}
			
			regionType     = getPanel().getRegionTypeField();
			region         = getPanel().getRegionField();
			ptm            = getPanel().getPtmField();
			targetResidues = getPanel().getTargetResiduesField();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ValidationState.OK;
	}
	
}
