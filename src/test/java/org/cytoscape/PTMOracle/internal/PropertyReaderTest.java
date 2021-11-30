package org.cytoscape.PTMOracle.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.cytoscape.PTMOracle.internal.io.PropertyReader;
import org.cytoscape.PTMOracle.internal.io.read.PropertyTSVReaderImpl;
import org.cytoscape.PTMOracle.internal.io.read.PropertyXMLReaderImpl;
import org.cytoscape.PTMOracle.internal.model.EdgeProperty;
import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.PropertyCollection;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.model.property.EdgePropertyImpl;
import org.cytoscape.PTMOracle.internal.model.property.NodePropertyImpl;
import org.cytoscape.PTMOracle.internal.model.property.PropertyCollectionImpl;
import org.cytoscape.PTMOracle.internal.model.property.PropertyMapImpl;
import org.cytoscape.work.TaskMonitor;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class PropertyReaderTest {

	private File validTsvFile;
	private File invalidTsvFile;
	private File validXmlFile;
	private File invalidXmlFile;

	private NodeProperty nodeProperty_1;
	private NodeProperty nodeProperty_2;
	private NodeProperty nodeProperty_3;
	private NodeProperty nodeProperty_4;
	
	private PropertyMap map_1;
	private PropertyMap map_2;

	@Before
	public void setUp() {
		validTsvFile   = new File("src/test/resources/valid_TSV_Example_input.txt");
		invalidTsvFile = new File("src/test/resources/invalid_TSV_Example_input.txt");
		
		validXmlFile   = new File("src/test/resources/valid_XML_Example_input.xml");
		invalidXmlFile = new File("src/test/resources/invalid_Xml_Example_input.xml");

		nodeProperty_1 = new NodePropertyImpl("Domain", "ERCC4", 824, 966, "-", "Good", "Pfam");
		nodeProperty_2 = new NodePropertyImpl("PTM", "Phosphoserine", 1071, 1071, "S", "Good", "Uniprot");
		nodeProperty_3 = new NodePropertyImpl("Sequence", "MSQLFYQGDSDDELQEELTRQTTQASQSSKIKNEDEPDDSNHLNEVENEDSKVLDDDAVLY", 1, 61, "-", "Good", "");
		nodeProperty_4 = new NodePropertyImpl("Sequence", "MVSLTFKNFKKEKVPLDLEPSNTILETKTKLAQSISCEESQIKLIYSGKVLQDSKTVSECGLK", 1, 63, "-", "", "Uniprot");
		
		map_1 = new PropertyMapImpl();
		map_2 = new PropertyMapImpl();
		
		map_1.addProperty(nodeProperty_1);
		map_1.addProperty(nodeProperty_2);
		map_1.addProperty(nodeProperty_3);
		map_2.addProperty(nodeProperty_4);
	}
	
	@Test
	public void testValidTSVReader() throws Exception {
		TaskMonitor tm = mock(TaskMonitor.class);
		PropertyReader reader = new PropertyTSVReaderImpl(validTsvFile);
		
		reader.run(tm);

		PropertyMap testMap_1 = reader.getInitialPropertyCollection().getPropertyMap("P06777");
		PropertyMap testMap_2 = reader.getInitialPropertyCollection().getPropertyMap("P32628");		

		// Check both cases
		assertTrue(testMap_1.getAllPropertyTypes().containsAll(map_1.getAllPropertyTypes()) && map_1.getAllPropertyTypes().containsAll(testMap_1.getAllPropertyTypes()));
		NodeProperty testProperty_1 = (NodeProperty) testMap_1.getPropertiesByType("Sequence").iterator().next();
		assertTrue(testProperty_1.getStatus().equals(nodeProperty_3.getStatus()) && testProperty_1.getComments().equals(nodeProperty_3.getComments()));
		
		assertFalse(testMap_2.getAllPropertyTypes().containsAll(map_2.getAllPropertyTypes()) && map_2.getAllPropertyTypes().containsAll(testMap_2.getAllPropertyTypes()));
		NodeProperty testProperty_2 = (NodeProperty) testMap_2.getPropertiesByType("Sequence").iterator().next();
		assertTrue(testProperty_2.getStatus().equals(nodeProperty_4.getStatus()) && testProperty_2.getComments().equals(nodeProperty_4.getComments()));
	}
	
	@Test(expected=Exception.class)
	public void testInvalidTsvReader() throws Exception {
		TaskMonitor tm = mock(TaskMonitor.class);
		PropertyReader reader = new PropertyTSVReaderImpl(invalidTsvFile);
		
		reader.run(tm);
	}
	
	@Test(expected=Exception.class)
	public void testInvalidXmlReader() throws Exception {
		TaskMonitor tm = mock(TaskMonitor.class);
		PropertyReader reader = new PropertyXMLReaderImpl(invalidXmlFile);
		
		reader.run(tm);
	}

	@Test
	public void testValidXmlReader() throws Exception {
		TaskMonitor tm = mock(TaskMonitor.class);
		PropertyReader reader = new PropertyXMLReaderImpl(validXmlFile);
		
		reader.run(tm);

		PropertyMap testMap_1 = reader.getInitialPropertyCollection().getPropertyMap("P06777");
		PropertyMap testMap_2 = reader.getInitialPropertyCollection().getPropertyMap("P32628");		

		// Check both cases
		assertTrue(testMap_1.getAllPropertyTypes().containsAll(map_1.getAllPropertyTypes()) && map_1.getAllPropertyTypes().containsAll(testMap_1.getAllPropertyTypes()));
		NodeProperty testProperty_1 = (NodeProperty) testMap_1.getPropertiesByType("Sequence").iterator().next();
		assertTrue(testProperty_1.getStatus().equals(nodeProperty_3.getStatus()) && testProperty_1.getComments().equals(nodeProperty_3.getComments()));
		
		assertFalse(testMap_2.getAllPropertyTypes().containsAll(map_2.getAllPropertyTypes()) && map_2.getAllPropertyTypes().containsAll(testMap_2.getAllPropertyTypes()));
		NodeProperty testProperty_2 = (NodeProperty) testMap_2.getPropertiesByType("Sequence").iterator().next();
		assertTrue(testProperty_2.getStatus().equals(nodeProperty_4.getStatus()) && testProperty_2.getComments().equals(nodeProperty_4.getComments()));
	}
	
}
