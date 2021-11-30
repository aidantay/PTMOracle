package org.cytoscape.PTMOracle.internal.model.util;

import java.util.Comparator;

import org.cytoscape.PTMOracle.internal.model.NodeProperty;

/**
 * NodePropertyComparator is a comparator for node properties.
 * Comparator currently works for point node properties.
 * TODO Extend comparator to interval node properties.
 * TODO Extend comparator to generic properties.
 * @author aidan
 */
public class NodePropertyComparator implements Comparator<NodeProperty> {

	@Override
	public int compare(NodeProperty o1, NodeProperty o2) {
		
		if (o1.getStartPosition() < o2.getStartPosition()) {
			return -1;

		} else if (o1.getStartPosition() > o2.getStartPosition()) {
			return 1;
			
		}
		return 0;
	}
		
}
