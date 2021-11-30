package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.cytoscape.PTMOracle.internal.util.swing.ListDisplay;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

/**
 * ListDisplay for showing keywords recognised by the Oracle.
 * @author aidan
 */
public class KeywordListDisplay extends ListDisplay {

	private static final long serialVersionUID = 8512525500455969320L;

	public KeywordListDisplay(List<?> conditions) {
		super(conditions);
	}
	
	public KeywordListDisplay() {
		super();
	}

	public Vector<String> getValues() {
		Set<String> values = new HashSet<String>();
		
		for (Object primaryKey : getOracle().getKeywordTable().getPrimaryKeys()) {
			Integer id = (Integer) primaryKey;
			List<?> compositeKey = getOracle().getKeywordTable().getCompositeKey(id);
			String keyword       = (String) compositeKey.get(0);
			String type          = (String) compositeKey.get(1);

			if (!getConditions().isEmpty()) {
				String propertyType = (String) getConditions().get(0);
				if (type.equals(propertyType)) {
					values.add(keyword);
				}
			}
		}
		
		List<String> sortedValues = new ArrayList<String>(values);
		Collections.sort(sortedValues);
		return new Vector<String>(sortedValues);
	}

}
