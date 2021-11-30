package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.cytoscape.PTMOracle.internal.util.swing.ComboBoxDisplay;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

/**
 * ComboBoxDisplay for showing properties recognised by the Oracle.
 * @author aidan
 */
public class PropertyComboBoxDisplay extends ComboBoxDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	public PropertyComboBoxDisplay(List<?> conditions) {
		super(conditions);
	}

	public PropertyComboBoxDisplay() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Vector<String> getValues() {
		Set<String> values = new HashSet<String>();
		Set<String> propertyTypes = (Set<String>) getOracle().getPropertyTable().getPrimaryKeys();
		for (String type : propertyTypes) {
			if (!getConditions().isEmpty()) {
				boolean isNodeProperty = (Boolean) getConditions().get(0);
				boolean isInterval     = (getConditions().size() == 2) ? (Boolean) getConditions().get(1) : false;
				
				if (isNodeProperty == getOracle().getPropertyTable().getNodePropertyFlag(type) && 
					isInterval == getOracle().getPropertyTable().getIntervalFlag(type)) {
					values.add(type);
				}
				
			} else {
				values.add(type);				
			}
		}

		List<String> sortedValues = new ArrayList<String>(values);
		Collections.sort(sortedValues);
		return new Vector<String>(sortedValues);
	}

}
