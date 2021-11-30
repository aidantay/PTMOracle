package org.cytoscape.PTMOracle.internal.preferences.tasks;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getNetworkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.cytoscape.PTMOracle.internal.preferences.AbstractInsertTableRowTask;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.util.ListSingleSelection;

/**
 * Task adding row into NetworkMappingTableDisplay in the Preferences menu
 * @author 
 */
public class AddNetworkMappingRowTask extends AbstractInsertTableRowTask {

	@Tunable(description="Root network name", dependsOn="numNetworks!=0", groups={"Required fields"})
	public ListSingleSelection<String> rootNetworkName;

	@Tunable(description="Source", dependsOn="numSources!=0", groups={"Required fields"})
	public ListSingleSelection<String> source;
	
	private int numNetworks;
	private int numSources;
	
	public AddNetworkMappingRowTask(TableDisplay table) {
		super(table);
		
		this.rootNetworkName = getRootNetworkNames();
		this.source          = getSources();
	}
	
	private ListSingleSelection<String> getRootNetworkNames() {		
		Set<String> rootNetworkNames = new HashSet<String>();
		for (CyNetwork network : getNetworkManager().getNetworkSet()) {
			rootNetworkNames.add(network.toString());
		}
		List<String> sortedValues = new ArrayList<String>(rootNetworkNames);
		Collections.sort(sortedValues);
		
		setNumNetworks(sortedValues.size());
		if (getNumNetworks() == 0) {
			sortedValues.add("No Networks");			
		}

		return new ListSingleSelection<String>(sortedValues);
	}
		
	private ListSingleSelection<String> getSources() {
		Set<String> sources = new HashSet<String>();
		sources.addAll(getOracle().getEdgePropertyTable().getAllSources());
		sources.addAll(getOracle().getNodePropertyTable().getAllSources());
		List<String> sortedValues = new ArrayList<String>(sources);
		Collections.sort(sortedValues);

		setNumSources(sortedValues.size());
		if (getNumSources() == 0) {
			sortedValues.add("No Sources");			
		}
		
		return new ListSingleSelection<String>(sortedValues);
	}
		
	@ProvidesTitle
	public String getMenuTitle() {
		return "Network Mapping Attributes";
	}
	
	public String getRootNetworkName() {
		return rootNetworkName.getSelectedValue();
	}

	public String getSource() {
		return source.getSelectedValue();
	}
	
	public int getNumNetworks() {
		return numNetworks;
	}
	
	public void setNumNetworks(int numNetworks) {
		this.numNetworks = numNetworks;
	}
	
	public int getNumSources() {
		return numSources;
	}
	
	public void setNumSources(int numSources) {
		this.numSources = numSources;
	}	

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public void run(TaskMonitor taskMonitor) throws Exception {
		Vector row = new Vector();
		row.add(null);
		row.add(getRootNetworkName());
		row.add(getSource());
		getTable().getModel().addRow(row);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public ValidationState getValidationState(Appendable errMsg) {
		try {
			// Check that the rootNetworkName and source are valid,
			if (numNetworks == 0 ||
				numSources  == 0) {
				errMsg.append("Invalid table contents.\nPlease see manual.");
				return ValidationState.INVALID;				
			}
			
			Vector updatedRows = (Vector) getTable().getModel().getDataVector();
			for (Object o : updatedRows) {
				Vector row = (Vector) o;
				String currRootNetworkName = (String) row.get(1);
				String currSource          = (String) row.get(2);

				// Check that the (rootNetworkName, source) pair is unique (i.e. we do not have one currently in the table) 				
				if (currRootNetworkName.equals(getRootNetworkName()) 
					&& currSource.equals(getSource())) {
					errMsg.append("Invalid table contents.\nPlease see manual.");
					return ValidationState.INVALID;					
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ValidationState.OK;
	}
	
}