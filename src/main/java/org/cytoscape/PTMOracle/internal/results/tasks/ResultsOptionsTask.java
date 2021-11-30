package org.cytoscape.PTMOracle.internal.results.tasks;

import java.util.ArrayList;
import java.util.List;

import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.util.ListSingleSelection;

/**
 * Task for modifying OracleResults options.
 * @author 
 */
public class ResultsOptionsTask extends AbstractTask {

	@Tunable(description="Delimiter:")
	public String delimiterField;
	
	@Tunable(description="Column to search:")
	public ListSingleSelection<String> searchColumnNameField;
	
	private static String delimiter        = "\\s"; 				// By default, delimiter is whitespace
	private static String searchColumnName = "Node: shared name";	// By default, column name is shared name
	
	public ResultsOptionsTask() {
		super();
		
		this.delimiterField = delimiter;
		this.searchColumnNameField = getNetworkColumnNames();
		this.searchColumnNameField.setSelectedValue(searchColumnName);
	}
	
	private ListSingleSelection<String> getNetworkColumnNames() {

		List<String> searchColumnNames = new ArrayList<String>();

		searchColumnNames = new ArrayList<String>();
		searchColumnNames.add("Node: " + CyRootNetwork.SHARED_NAME);
		searchColumnNames.add("Node: " + CyRootNetwork.SUID);
		searchColumnNames.add("Edge: " + CyRootNetwork.SHARED_INTERACTION);
		searchColumnNames.add("Edge: " + CyRootNetwork.SHARED_NAME);
		searchColumnNames.add("Edge: " + CyRootNetwork.SUID);
		
		return new ListSingleSelection<String>(searchColumnNames);
	}
	
	@ProvidesTitle
	public String getMenuTitle() {
		return "OracleResults Options";
	}

	public String getDelimiter() {
		return delimiter;
	}

	public String getSearchColumnName() {
		return searchColumnName;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Updating results options");
		taskMonitor.setStatusMessage("Options updated for OracleResult");
		
		delimiter = delimiterField;
		searchColumnName = searchColumnNameField.getSelectedValue();
	}	
	
}