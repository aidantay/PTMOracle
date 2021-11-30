package org.cytoscape.PTMOracle.internal.io.write;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cytoscape.PTMOracle.internal.io.AbstractPropertyWriter;
import org.cytoscape.PTMOracle.internal.io.PropertyWriter;
import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyCollection;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.work.TaskMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * PropertyXMLWriter composes properties for NODEs and EDGES into a XML-based file
 * @author aidan
 */
public class PropertyXMLWriterImpl extends AbstractPropertyWriter implements PropertyWriter {

	private PropertyCollection propertyCollection;
	
	public PropertyXMLWriterImpl(File file, PropertyCollection propertyCollection) {
		super(file);
		
		this.propertyCollection = propertyCollection;
	}
		
	public PropertyCollection getPropertyCollection() {
		return propertyCollection;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Writing properties");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder        = factory.newDocumentBuilder();
		Document doc                   = builder.newDocument();

		Element root = doc.createElement(ORACLE_LINE);
    	root.setAttribute("version", "1.0");

		appendNodeLines(doc, root);
		doc.appendChild(root);
		
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		tr.transform(new DOMSource(doc), new StreamResult(getFile()));
	}
	
	private void appendNodeLines(Document doc, Element root) {
		
		for (String accession : getPropertyCollection().getAllIds()) {
			PropertyMap propertyMap = getPropertyCollection().getPropertyMap(accession);
			
			Element nodeLine = doc.createElement(NODE_LINE);
			nodeLine.setAttribute(ID_TAG, accession);
			
			appendPropertyLines(doc, nodeLine, propertyMap);
			root.appendChild(nodeLine);
		}
	}
	
	private void appendPropertyLines(Document doc, Element nodeLine, PropertyMap propertyMap) {
		Element propertiesLine = doc.createElement(PROPERTIES_LINE);
		
		for (String type : propertyMap.getAllPropertyTypes()) {
			for (Property p : propertyMap.getPropertiesByType(type)) {
				NodeProperty nodeProperty = (NodeProperty) p;

				Element propertyLine = doc.createElement(PROPERTY_LINE);
				propertyLine.setAttribute(TYPE_TAG, type);
				propertyLine.setAttribute(DESCRIPTION_TAG, nodeProperty.getDescription());
							
				Element positionLine = doc.createElement(POSITION_LINE);
				positionLine.setAttribute(START_TAG, String.valueOf(nodeProperty.getStartPosition()));
				positionLine.setAttribute(END_TAG, String.valueOf(nodeProperty.getEndPosition()));
				positionLine.setAttribute(RESIDUE_TAG, nodeProperty.getResidue());
			
				propertyLine.appendChild(positionLine);
				propertiesLine.appendChild(propertyLine);
			}
		}

		if (propertiesLine.hasChildNodes()) {
			nodeLine.appendChild(propertiesLine);
		}
	}

}
