package org.cytoscape.PTMOracle.internal;

import static org.junit.Assert.*;

import org.cytoscape.PTMOracle.internal.model.EdgeProperty;
import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.PropertyCollection;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.model.property.EdgePropertyImpl;
import org.cytoscape.PTMOracle.internal.model.property.NodePropertyImpl;
import org.cytoscape.PTMOracle.internal.model.property.PropertyCollectionImpl;
import org.cytoscape.PTMOracle.internal.model.property.PropertyMapImpl;
import org.junit.Before;
import org.junit.Test;

public class PropertyTest {

	private NodeProperty nodeProperty_1;
	private NodeProperty nodeProperty_2;
	private NodeProperty nodeProperty_3;
	private NodeProperty nodeProperty_4;
	private NodeProperty nodeProperty_5;
	
	private EdgeProperty edgeProperty_1;
	private EdgeProperty edgeProperty_2;
	
	private PropertyMap map_1;
	
	private PropertyCollection collection;

	@Before
	public void setUp() {
		nodeProperty_1 = new NodePropertyImpl("PTM", "test", 1, 2, "R", "status1", "comments1");
		nodeProperty_2 = new NodePropertyImpl("PTM", "test", 1, 2, "R", "status2", "comments2");
		nodeProperty_3 = new NodePropertyImpl("Domain", "test", 1, 2, "R");
		nodeProperty_4 = new NodePropertyImpl("Motif", "test", 1, 2, "R", "", "");
		nodeProperty_5 = new NodePropertyImpl("Motif", "test", 1, 2, "R", "", "");
		
		edgeProperty_1 = new EdgePropertyImpl("DDI", "test", "status1", "comments1");
		edgeProperty_2 = new EdgePropertyImpl("DMI", "test", "status2", "comments2");
		
		map_1 = new PropertyMapImpl();
		
		collection = new PropertyCollectionImpl();
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidProperties() {
		new NodePropertyImpl("", "", -1, -6, "ASD", null, null);
		new NodePropertyImpl("", "", -1, -6, null, null, null);
		new EdgePropertyImpl("", "", null, null);
	}
	
	@Test
	public void testPropertyMap() {
		assertTrue(nodeProperty_1.equals(nodeProperty_2));
		assertFalse(nodeProperty_1.equals(nodeProperty_3));
		
		map_1.addProperty(nodeProperty_1);
		assertTrue(map_1.containsProperty(nodeProperty_1));
		assertTrue(map_1.containsPropertyType("PTM"));
		assertFalse(map_1.containsPropertyType("Domain"));
		map_1.addProperty(nodeProperty_2);
		map_1.addProperty(nodeProperty_3);
		map_1.addProperty(nodeProperty_4);
		map_1.addProperty(nodeProperty_5);
		map_1.addProperty(edgeProperty_1);
		map_1.addProperty(edgeProperty_2);
		
		assertTrue(map_1.getAllPropertyTypes().size() == 5);
		assertTrue(map_1.getPropertiesByType("PTM").size() == 1);
		assertTrue(map_1.getPropertiesByType("PTM").iterator().next().getStatus().equals("status1, status2"));
		assertTrue(map_1.getPropertiesByType("Motif").iterator().next().getStatus().equals(""));
	}
	
	@Test
	public void testCollection() {
		collection.addProperty("ID1", nodeProperty_1);
		collection.addProperty("ID2", nodeProperty_1);
		
		assertTrue(collection.containsId("ID1"));
		assertTrue(collection.getAllIds().size() == 2);
		assertTrue(collection.getPropertyMap("ID1").getAllPropertyTypes().equals(collection.getPropertyMap("ID2").getAllPropertyTypes()));
	}
	
}
