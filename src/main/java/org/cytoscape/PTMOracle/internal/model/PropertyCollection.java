package org.cytoscape.PTMOracle.internal.model;

import java.util.Set;

/**
 * PropertyCollection is a map of all PropertyMaps for all nodes or edges.
 * @author aidan
 */
public interface PropertyCollection {

	public static final String DELIMITER       = ":_:";
	
	public boolean containsId(String id);
	public boolean isNodeId(String id);
	
	public String getDelimiter();
	public Set<String> getAllIds();
	public PropertyMap getPropertyMap(String id);
	
	public void addProperty(String id, Property property);
	public void addProperty(String sourceId, String targetId, Property property);

}