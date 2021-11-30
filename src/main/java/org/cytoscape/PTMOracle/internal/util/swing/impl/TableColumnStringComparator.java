package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.util.Comparator;

public class TableColumnStringComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		return String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
	}
		
}
