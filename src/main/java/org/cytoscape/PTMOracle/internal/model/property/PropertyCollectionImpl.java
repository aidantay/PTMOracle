package org.cytoscape.PTMOracle.internal.model.property;

import java.util.HashMap;
import java.util.Set;

import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyCollection;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;

/**
 * PropertyCollection is a map of all PropertyMaps for all nodes or edges.
 * @author aidan
 */
public class PropertyCollectionImpl extends HashMap<String, PropertyMap> implements PropertyCollection {

	private static final long serialVersionUID = 3905178384813782479L;
	
	public PropertyCollectionImpl() {
		super();
	}
	
	@Override
	public boolean containsId(String id) {
		return containsKey(id);
	}
	
	@Override
	public boolean isNodeId(String id) {
		if (id.contains(getDelimiter())) {
			return false;
		}
		return true;
	}
	
	@Override
	public String getDelimiter() {
		return DELIMITER;
	}
	
	@Override
	public Set<String> getAllIds() {
		return keySet();
	}
	
	@Override
	public PropertyMap getPropertyMap(String id) {
		PropertyMap propertyMap = get(id);
		if (propertyMap == null) {
			propertyMap = new PropertyMapImpl();
		}
		return propertyMap;
	}
	
	@Override
	public void addProperty(String id, Property property) {
		PropertyMap propertyMap = getPropertyMap(id);
		propertyMap.addProperty(property);
		put(id, propertyMap);
	}
	
	@Override
	public void addProperty(String sourceId, String targetId, Property property) {
		String id = sourceId + getDelimiter() + targetId;
		addProperty(id, property);
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		for (String id : keySet()) {
			out.append(id);
			out.append(System.getProperty("line.separator"));
			out.append(getPropertyMap(id));
		}
		return out.toString();
	}
	
}