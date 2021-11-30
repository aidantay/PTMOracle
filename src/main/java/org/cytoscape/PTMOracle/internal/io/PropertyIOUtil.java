package org.cytoscape.PTMOracle.internal.io;

/**
 * PropertyIO contains static variables for PropertyReaders and PropertyWriters.
 * @author aidan
 */
public interface PropertyIOUtil {
	
	public static final String ORACLE_LINE     = "PTMOracle";
	public static final String NODE_LINE       = "node";
	public static final String EDGE_LINE       = "edge";
	public static final String PROPERTIES_LINE = "properties";
	public static final String PROPERTY_LINE   = "property";
	public static final String POSITION_LINE   = "positionOnProtein";
	public static final String SOURCE_LINE     = "sourceProperty";
	public static final String TARGET_LINE     = "targetProperty";
	
	public static final String STATUS_LINE     = "annotationStatus";
	public static final String COMMENTS_LINE   = "additionalComments";
	public static final String SEQUENCE_LINE   = "sequence";
		    	
	public static final String SOURCE_TAG      = "source";
	public static final String TARGET_TAG      = "target";
	public static final String ID_TAG          = "id";
	public static final String TYPE_TAG        = "type";
	public static final String DESCRIPTION_TAG = "description";
	public static final String START_TAG       = "startPos";
	public static final String END_TAG         = "endPos";
	public static final String RESIDUE_TAG     = "residue";

}
