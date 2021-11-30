package org.cytoscape.PTMOracle.internal.schema.tasks;

import java.io.IOException;
import java.util.Set;

import org.cytoscape.PTMOracle.internal.io.PropertyReader;
import org.cytoscape.PTMOracle.internal.schema.swing.ImportPropertiesPanel;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.TunableValidator;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

/**
 * Task for importing oracle files.
 * @author aidan
 */
public class ImportPropertiesIntoOracleTask extends AbstractRootNetworkTask implements TunableValidator {

	@Tunable
	public ImportPropertiesPanel panel;

	private PropertyReader reader;
	
	public ImportPropertiesIntoOracleTask(CyNetwork network, PropertyReader reader) {
		
		super(network);
		
		this.reader = reader;
		this.panel  = new ImportPropertiesPanel(network, reader.getFile(), reader.getPossibleKeywordMap());
	}

	@ProvidesTitle
	public String getMenuTitle() {
		return "Import";
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Resolving unmapped properties");
		
		if (reader.getPossibleKeywordMap().size() != 0) {
			reader.setUnresolvedProperties(panel.getTableDisplay());
		}
		
		UpdateOracleTask updateOracleTask = new UpdateOracleTask(network, reader.getResolvedPropertyCollection(), 
				panel.getSourceString(), panel.getAttribute());
		
		UpdateCytoscapeTask updateCytoscapeTask = new UpdateCytoscapeTask();
		
		TaskIterator iterator = new TaskIterator();
		iterator.append(updateOracleTask);
		iterator.append(updateCytoscapeTask);
		insertTasksAfterCurrentTask(iterator);
	}

	@Override
	public ValidationState getValidationState(Appendable errMsg) {
		try {
			Set<String> sources = getOracle().getNetworkMappingTable().getSources(getRootNetwork().toString());
			
			if (panel.getSourceString().length() == 0) {
				errMsg.append("Invalid parameters. Input source string.");
				return ValidationState.INVALID;
			}

			if (sources.contains(panel.getSourceString())) {
				errMsg.append("Importing from the same source will overwrite previous properties.\n");
				errMsg.append("This may affect properties in other networks.\n");
				errMsg.append("Do you wish to continue?");
				return ValidationState.REQUEST_CONFIRMATION;		
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ValidationState.OK;
	}
}
