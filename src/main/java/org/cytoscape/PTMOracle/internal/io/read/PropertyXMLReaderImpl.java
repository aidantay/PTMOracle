package org.cytoscape.PTMOracle.internal.io.read;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cytoscape.PTMOracle.internal.io.AbstractPropertyReader;
import org.cytoscape.PTMOracle.internal.io.PropertyReader;
import org.cytoscape.PTMOracle.internal.model.EdgeProperty;
import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.property.EdgePropertyImpl;
import org.cytoscape.PTMOracle.internal.model.property.NodePropertyImpl;
import org.cytoscape.work.TaskMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * PropertyXMLReader parses properties for NODEs and EDGES in a XML-based file
 * @author aidan
 */
public class PropertyXMLReaderImpl extends AbstractPropertyReader implements PropertyReader {

	public PropertyXMLReaderImpl(File file) {
		super(file);
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder        = factory.newDocumentBuilder();
		Document doc                   = builder.parse(getFile());

		parseNodes(doc);
		parseEdges(doc);
	}
	
	private void parseNodes(Document doc) {
		NodeList nodeLineList = doc.getElementsByTagName(NODE_LINE);

		for (int i = 0; i < nodeLineList.getLength(); i++) {
			Element nodeLine = (Element) nodeLineList.item(i);
			String accession = nodeLine.getAttribute(ID_TAG);
			parseNodeProperties(nodeLine, accession);
		}
	}
	
	private void parseNodeProperties(Element node, String accession) {
		NodeList propertyLineList = node.getElementsByTagName(PROPERTY_LINE);
		
		for (int i = 0; i < propertyLineList.getLength(); i++) {
			Element propertyLine = (Element) propertyLineList.item(i);
			String type        = propertyLine.getAttribute(TYPE_TAG);
			String description = propertyLine.getAttribute(DESCRIPTION_TAG);
			int startPos       = 0;
			int endPos         = 0;
			String residue     = "";
			String status      = "";
			String comments    = "";

			NodeList propertyMetadataList = propertyLine.getChildNodes();
			for (int j = 0; j < propertyMetadataList.getLength(); j++) {
				Node n = propertyMetadataList.item(j);
				// Some nodes in the DOM parser are not elements..Not sure why
				// Just do a check for it anyway
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element propertyMetadata = (Element) n;
					
					if (propertyMetadata.getNodeName().equals(POSITION_LINE)) {
						startPos = Integer.valueOf(propertyMetadata.getAttribute(START_TAG));
						endPos   = Integer.valueOf(propertyMetadata.getAttribute(END_TAG));
						residue  = propertyMetadata.getAttribute(RESIDUE_TAG);
						
					} else if (propertyMetadata.getNodeName().equals(STATUS_LINE)) {
						status = propertyMetadata.getTextContent();
						
					} else if (propertyMetadata.getNodeName().equals(COMMENTS_LINE)) {
						comments = propertyMetadata.getTextContent();
					}
				}
			}
			
			NodeProperty property = new NodePropertyImpl(type, description, startPos, endPos, residue, status, comments);
			getInitialPropertyCollection().addProperty(accession, property);
		}
	}

	private void parseEdges(Document doc) {
		NodeList edgeLineList = doc.getElementsByTagName(EDGE_LINE);
		
		for (int i = 0; i < edgeLineList.getLength(); i++) {
			Element edgeLine = (Element) edgeLineList.item(i);
			String sourceId = edgeLine.getAttribute(SOURCE_TAG);
			String targetId = edgeLine.getAttribute(TARGET_TAG);
			parseEdgeProperties(edgeLine, sourceId, targetId);
		}
	}
	
	private void parseEdgeProperties(Element edge, String sourceId, String targetId) {
		NodeList propertyLineList = edge.getElementsByTagName(PROPERTY_LINE);

		for (int i = 0; i < propertyLineList.getLength(); i ++) {
			Element propertyLine = (Element) propertyLineList.item(i);
			String type        = propertyLine.getAttribute(TYPE_TAG);
			String description = propertyLine.getAttribute(DESCRIPTION_TAG);

			String status   = "";
			String comments = "";

			NodeList propertyMetadataList = propertyLine.getChildNodes();
			for (int j = 0; j < propertyMetadataList.getLength(); j++) {
				Node n = propertyMetadataList.item(j);
				// Some nodes are not elements..Not sure why
				// Just do a check for it anyway
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element propertyMetadata = (Element) n;
					
					if (propertyMetadata.getNodeName().equals(STATUS_LINE)) {
						status = propertyMetadata.getTextContent();
						
					} else if (propertyMetadata.getNodeName().equals(COMMENTS_LINE)) {
						comments = propertyMetadata.getTextContent();
					}
				}
			}

			EdgeProperty property = new EdgePropertyImpl(type, description, status, comments);
			getInitialPropertyCollection().addProperty(sourceId, targetId, property);
		}
	}
	
}
