package org.cytoscape.PTMOracle.internal.tools.tasks;

import java.io.IOException;
import java.util.List;

import org.cytoscape.PTMOracle.internal.tools.swing.CalculatorPanel;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.TunableValidator;

/**
 * Task for the Calculator OracleTool menu, which counts properties on proteins.
 * OracleTool for node and edges.
 * TODO Extend to multiple property types. Requires redesign of panel layout
 * @author aidan
 */
public class CalculatorTask extends AbstractRootNetworkTask implements TunableValidator {

	@Tunable
	public CalculatorPanel panel;
	
	private String propertyType;
	private List<String> keywordList;
	
	public CalculatorTask(CyNetwork network) {
		super(network);
		
		this.panel = new CalculatorPanel();
	}
	
	public CalculatorPanel getPanel() {
		return panel;
	}

	@ProvidesTitle
	public String getMenuTitle() {
		return "Calculator";
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Calculating property counts");
		taskMonitor.setStatusMessage("Counting properties in network");

		CountKeywordTask task = new CountKeywordTask(network, propertyType, keywordList);
		insertTasksAfterCurrentTask(task);
	}
	
	@Override
	public ValidationState getValidationState(Appendable errMsg) {

		try {
			if (getPanel().getKeywordListField() == null) {
				errMsg.append("Invalid parameters");
				return ValidationState.INVALID;
			}
						
			propertyType = getPanel().getPropertyTypeField();
			keywordList  = getPanel().getKeywordListField();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ValidationState.OK;
	}

}
