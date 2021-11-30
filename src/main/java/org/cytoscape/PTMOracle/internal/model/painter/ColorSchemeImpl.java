package org.cytoscape.PTMOracle.internal.model.painter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.model.ColorScheme;

/**
 * ColorScheme is a map of painter parameters associated for NODEs 
 * @author aidan
 */
public class ColorSchemeImpl extends HashMap<Pair<String, String>, Color> implements ColorScheme {

	private static final long serialVersionUID = 9116083081963627842L;

	public ColorSchemeImpl() {
		super();
	}
	
	public void addColor(Pair<String, String> values, Color color) {
		put(values, color);
	}
	
	public Set<Pair<String, String>> getAllValues() {
		return keySet();
	}
	
	public Color getColor(Pair<String, String> values) {
		return get(values);
	}

	public List<Pair<String, String>> getSortedValues() {
		List<Pair<String, String>> sortedKeys = new ArrayList<Pair<String, String>>(getAllValues());
		Collections.sort(sortedKeys, new PairComparator());
		return sortedKeys;
	}
	
	private class PairComparator implements Comparator<Pair<String, String>> {

		@Override
		public int compare(Pair<String, String> o1, Pair<String, String> o2) {
			String o1_left  = o1.getLeft();
			String o1_right = o1.getRight();
			
			String o2_left  = o2.getLeft();
			String o2_right = o2.getRight();

			if (String.CASE_INSENSITIVE_ORDER.compare(o1_left, o2_left) == 0) {
				return String.CASE_INSENSITIVE_ORDER.compare(o1_right, o2_right); 
			}		
			return String.CASE_INSENSITIVE_ORDER.compare(o1_left, o2_left);
		}
			
	}

}