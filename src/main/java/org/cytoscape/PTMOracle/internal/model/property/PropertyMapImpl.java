package org.cytoscape.PTMOracle.internal.model.property;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;

import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.SEQUENCE;

/**
 * PropertyMap is a map of all properties associated with a single node or edge.
 * @author aidan
 */
public class PropertyMapImpl extends HashMap<String, Set<Property>> implements PropertyMap {

	private static final long serialVersionUID = -5397276376234238103L;

	public PropertyMapImpl() {
		super();
	}
	
 	@Override
 	public void putAll(PropertyMap m) {
 		for (String propertyType : m.getAllPropertyTypes()) {
 			for (Property property : m.getPropertiesByType(propertyType)) {
 				addProperty(property);
 			}
 		}
 	}
	
	@Override
	public boolean containsPropertyType(String type) {
		if (containsKey(type)) {
			Set<Property> propertySet = getPropertiesByType(type);
			if (!propertySet.isEmpty()) {
				return true;
			}
		}
		return false;
	}
		
	@Override
	public boolean containsProperty(Property property) {
		if (containsPropertyType(property.getType())) {
			Set<Property> propertySet = getPropertiesByType(property.getType());
			if (propertySet.contains(property)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasProperties() {
		if (isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public void addProperty(Property property) {
		Set<Property> propertySet = getPropertiesByType(property.getType());
		if (propertySet == null) {
			propertySet = new HashSet<Property>();
		}
		
		if (propertySet.contains(property)) {
			for (Property p : propertySet) {
				if (p.equals(property)) {
					String prevStatus   = p.getStatus();
					String prevComments = p.getComments();
					
					String currStatus   = (!prevStatus.isEmpty()) ? (prevStatus + ", " + property.getStatus()) : property.getStatus();  
					String currComments = (!prevComments.isEmpty()) ? (prevComments + ", " + property.getComments()) : property.getComments();  

					p.setStatus(currStatus);
					p.setComments(currComments);
				}
			}
			
		} else {
			propertySet.add(property);
		}
		put(property.getType(), propertySet);
	}
	
	@Override
	public void addPropertySet(String type, Set<Property> propertySet) {
		put(type, propertySet);
	}
	
	@Override
	public Set<String> getAllPropertyTypes() {
		return keySet();		
	}

	@Override
	public Set<Property> getPropertiesByType(String type) {
		return get(type);
	}
	
	@Override
	public boolean hasSequence() {
		Set<Property> propertySet = getPropertiesByType(SEQUENCE);
		if (propertySet != null) {
			if (propertySet.size() == 1) {
				return true;
			}
		}
		return false;		
	}
	
	@Override
	public Property getSequence() {
		Set<Property> propertySet = getPropertiesByType(SEQUENCE);
		return propertySet.iterator().next();
	}
	
	@Override
	public int getSequenceCount() {
		if (hasSequence()) {
			Set<Property> propertySet = getPropertiesByType(SEQUENCE);
			return propertySet.size();
		}
		return 0;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		
		for (String type : getAllPropertyTypes()) {
			out.append(type);
			out.append(System.getProperty("line.separator"));
			for (Property f : getPropertiesByType(type)) {
				out.append(f);
				out.append(System.getProperty("line.separator"));
			}
		}
		return out.toString();
	}

	
}