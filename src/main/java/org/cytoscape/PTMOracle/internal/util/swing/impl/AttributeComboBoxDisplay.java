package org.cytoscape.PTMOracle.internal.util.swing.impl;

import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getRootNetwork;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.cytoscape.PTMOracle.internal.util.swing.ComboBoxDisplay;
import org.cytoscape.model.CyColumn;

/**
 * ComboBoxDisplay for showing a list of selectable network columns for nodes
 * @author aidan
 */
public class AttributeComboBoxDisplay extends ComboBoxDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	public AttributeComboBoxDisplay() {
		super();
	}
	
	public Vector<String> getValues() {
		Set<String> values = new HashSet<String>(); 
		for (CyColumn column : getRootNetwork(getCurrentNetwork()).getSharedNodeTable().getColumns()) {
			values.add(column.getName());
		}
		List<String> sortedValues = new ArrayList<String>(values);
		Collections.sort(sortedValues);
		return new Vector<String>(sortedValues);
	}

}
