package org.cytoscape.PTMOracle.internal.io.write;

import java.awt.Color;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.io.PainterWriter;
import org.cytoscape.PTMOracle.internal.model.ColorScheme;
import org.cytoscape.work.TaskMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * PainterXMLWriter composes painter parameters for NODEs into a XML-based file
 * @author aidan
 */
public class PainterXMLWriterImpl implements PainterWriter {

	private File file;
	private ColorScheme colorScheme;
	
	public PainterXMLWriterImpl(File file, ColorScheme colorScheme) {
		this.file = file;
		this.colorScheme = colorScheme;
	}
	
 	@Override
	public File getFile() {
		return file;
	}
 	
 	@Override
 	public ColorScheme getColorScheme() {
 		return colorScheme;
 	}

 	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Exporting properties in painter");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder        = factory.newDocumentBuilder();
		Document doc                   = builder.newDocument();

		Element root = doc.createElement(ORACLE_PAINTER_LINE);
    	root.setAttribute("version", "1.0");

		appendPropertyLines(doc, root);
		doc.appendChild(root);
		
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		tr.transform(new DOMSource(doc), new StreamResult(getFile()));
 	}
 	
 	private void appendPropertyLines(Document doc, Element root) {
 		
        for (Pair<String, String> pair : getColorScheme().getAllValues()) {
	    	String attribute = pair.getLeft();
	    	String value     = pair.getRight();
	    	Color color      = getColorScheme().getColor(pair);
	    	
	    	Element propertyLine = doc.createElement(PROPERTY_LINE);
	    	propertyLine.setAttribute(RED_TAG, String.valueOf(color.getRed()));
	    	propertyLine.setAttribute(GREEN_TAG, String.valueOf(color.getGreen()));
	    	propertyLine.setAttribute(BLUE_TAG, String.valueOf(color.getBlue()));
	    	
	    	appendMappingLines(doc, propertyLine, attribute, value);
	    	root.appendChild(propertyLine);
		}
 	}
 	
 	private void appendMappingLines(Document doc, Element propertyLine, String attribute, String value) {
    	
    	if (!attribute.isEmpty()) {
     		Element attributeLine = doc.createElement(ATTRIBUTE_LINE);
     		attributeLine.appendChild(doc.createTextNode(attribute));
     		propertyLine.appendChild(attributeLine);
    	}
    	
 		Element valueLine = doc.createElement(VALUE_LINE);
 		valueLine.appendChild(doc.createTextNode(value));
 		propertyLine.appendChild(valueLine);
 		
 	}

	@Override
	public void cancel() {
		
	}
}
