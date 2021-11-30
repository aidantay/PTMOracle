package org.cytoscape.PTMOracle.internal.preferences.swing;

import java.util.Vector;

import org.cytoscape.PTMOracle.internal.util.swing.ListDisplay;

/**
 * ListDisplay for showing oracle preferences
 * @author aidan
 */
public class PreferencesListDisplay extends ListDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	public static final String PROPERTY      = "Properties";
	public static final String KEYWORD       = "Keywords";
	public static final String MAPPING       = "Mappings";
	
	// These are just for error checking 
	// Remove or hide when necessary TODO 
	public static final String ORACLE_SCHEMA = "Oracle Schema";
	public static final String NODE          = "NodeProperties";
	public static final String EDGE          = "EdgeProperties";
	
	public PreferencesListDisplay() {
		super();
	}
	
	public Vector<String> getValues() {
		Vector<String> values = new Vector<String>();

		values.add(PROPERTY);
		values.add(KEYWORD);
		values.add(MAPPING);
//		values.add(ORACLE_SCHEMA);
//		values.add(NODE);
//		values.add(EDGE);
		
		return values; 
	}
	
}
