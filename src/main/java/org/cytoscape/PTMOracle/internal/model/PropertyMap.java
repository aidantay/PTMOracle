package org.cytoscape.PTMOracle.internal.model;

import java.util.Set;

/**
 * PropertyMap is a map of all properties associated with a single node or edge.
 * @author aidan
 */
public interface PropertyMap {

	public void putAll(PropertyMap m);
	
	public boolean containsPropertyType(String type);
	public boolean containsProperty(Property property);
	public boolean hasProperties();
	public void addProperty(Property property);
	public void addPropertySet(String type, Set<Property> propertySet);
	public Set<String> getAllPropertyTypes();
	public Set<Property> getPropertiesByType(String type);
	
	public boolean hasSequence();
	public Property getSequence();
	public int getSequenceCount();
	
}