package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.util.Comparator;

public class TableColumnNumericComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer o1, Integer o2) {
		if (o1 < o2) {
			return -1;
		} else if (o1 > o2) {
			return 1;
		}
		return 0;
	}
		
}
