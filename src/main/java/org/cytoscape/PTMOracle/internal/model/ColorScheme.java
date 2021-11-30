package org.cytoscape.PTMOracle.internal.model;

import java.awt.Color;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

/**
 * ColorScheme is a map of painter parameters associated for NODEs 
 * @author aidan
 */
public interface ColorScheme {

	public void addColor(Pair<String, String> values, Color color);
	public Set<Pair<String, String>> getAllValues();
	public Color getColor(Pair<String, String> values);
	
	public List<Pair<String, String>> getSortedValues();
}