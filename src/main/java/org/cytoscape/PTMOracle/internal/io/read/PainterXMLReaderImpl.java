package org.cytoscape.PTMOracle.internal.io.read;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.io.PainterReader;
import org.cytoscape.work.TaskMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * PainterXMLReader parses painter parameters for NODEs in a XML-based file
 * @author aidan
 */
public class PainterXMLReaderImpl implements PainterReader {

	private File file;
	private Map<Pair<String, String>, Color> colorScheme;
	
	public PainterXMLReaderImpl(File file) {
		super();
		this.file = file;
		this.colorScheme = new HashMap<Pair<String, String>, Color>();
	}
	
	@Override
	public File getFile() {
		return file;
	}
	
	@Override
	public Map<Pair<String, String>, Color> getColorScheme() {
		return colorScheme;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Importing painter properties");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder        = factory.newDocumentBuilder();
		Document doc                   = builder.parse(getFile());

		parsePainterProperties(doc);
	}
	
	private void parsePainterProperties(Document doc) {
		NodeList propertyLineList = doc.getElementsByTagName(PROPERTY_LINE);
		
		for (int i = 0; i < propertyLineList.getLength(); i++) {
			Element propertyLine = (Element) propertyLineList.item(i);
    		int r            = Integer.valueOf(propertyLine.getAttribute(RED_TAG));
    		int g            = Integer.valueOf(propertyLine.getAttribute(GREEN_TAG));
    		int b            = Integer.valueOf(propertyLine.getAttribute(BLUE_TAG));
    		String attribute = "";
    		String value     = "";

    		NodeList propertyMetadataList = propertyLine.getChildNodes();
			for (int j = 0; j < propertyMetadataList.getLength(); j++) {
				Node n = propertyMetadataList.item(j);
				// Some nodes in the DOM parser are not elements..Not sure why
				// Just do a check for it anyway
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element propertyMetadata = (Element) n;
					
					if (propertyMetadata.getNodeName().equals(ATTRIBUTE_LINE)) {
						attribute = propertyMetadata.getTextContent();				
						
					} else if (propertyMetadata.getNodeName().equals(VALUE_LINE)) {
						value = propertyMetadata.getTextContent();
					}
				}
			}
			Pair<String, String> pair = new ImmutablePair<String, String>(attribute, value);
			Color color = new Color(r, g, b);
			getColorScheme().put(pair, color);
		}
	}

	@Override
	public void cancel() {
		
	}

}
