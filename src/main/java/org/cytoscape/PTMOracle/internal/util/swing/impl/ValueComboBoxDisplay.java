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
import org.cytoscape.model.CyTable;

/**
 * ComboBoxDisplay for showing a list of values in a specified network column for nodes.
 * @author aidan
 */
public class ValueComboBoxDisplay extends ComboBoxDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	public ValueComboBoxDisplay(List<?> conditions) {
		super(conditions);
	}
	
	public ValueComboBoxDisplay() {
		super();
	}
	
	public Vector<String> getValues() {
		Set<String> values = new HashSet<String>();
		
		if (getRootNetwork(getCurrentNetwork()) != null) {
			if (!getConditions().isEmpty()) {
				CyTable nodeTable = getRootNetwork(getCurrentNetwork()).getSharedNodeTable();
				CyColumn nodeColumn = nodeTable.getColumn((String) getConditions().get(0));
				for (Object o : nodeColumn.getValues(nodeColumn.getType())) {
					if (nodeColumn.getType().equals(List.class)) {
						@SuppressWarnings("unchecked")
						List<Object> list = (List<Object>) o;
						for (Object value : list) {
							values.add(String.valueOf(value));
						}
					} else {
						values.add(String.valueOf(o));
					}
				}

			}
		}
		List<String> sortedValues = new ArrayList<String>(values);
		Collections.sort(sortedValues);
		return new Vector<String>(sortedValues);
	}

}
