package org.cytoscape.PTMOracle.internal.results.swing;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.util.swing.TextDisplay;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;
import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.SEQUENCE;

/**
 * TextDisplay for showing the protein sequence for a given node and root network
 * TODO Extend property priorities displayed on the sequence. Potentially more sophisticated with type rankings [requires changes to Oracle table]. 
 * @author aidan
 */
public class ProteinSequenceTextDisplay extends TextDisplay {

	private static final long serialVersionUID = 4681680195787628795L;

	private String sharedNodeName;
	private String rootNetworkName;
	
	// Location of all properties in the sequence
	private Map<Integer, String> sequenceLocations;
	
	public ProteinSequenceTextDisplay(String sharedNodeName, String rootNetworkName) {
		super();
		
		this.sharedNodeName = sharedNodeName;
		this.rootNetworkName = rootNetworkName;
		this.sequenceLocations = new HashMap<Integer, String>();		
	}
	
	public String getSharedNodeName() {
		return sharedNodeName;
	}
	
	public String getRootNetworkName() {
		return rootNetworkName;
	}
	
	public Map<Integer, String> getSequenceLocations() {
		return sequenceLocations;
	}
	
	public void updateTextDisplay() {
		// If there is a sequence, then we can co-locate properties
		// Otherwise, theres no point....
		PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(getSharedNodeName(), getRootNetworkName(), true);
		if (uniquePropertyMap.hasSequence()) {
			String sequence = uniquePropertyMap.getSequence().getDescription();
			findPropertyLocations(uniquePropertyMap);
			formatSequence(sequence);
			
		} else {
			if (uniquePropertyMap.getSequenceCount() > 1) {
				formatSequence("Sequence conflict");
			}
		}
	}
	
	public void findPropertyLocations(PropertyMap uniquePropertyMap) {
		uniquePropertyMap = getOracle().getUniqueProperties(getSharedNodeName(), getRootNetworkName(), true);
		for (String type : uniquePropertyMap.getAllPropertyTypes()) {
			if (type.equals(SEQUENCE)) {
				continue;
			}
			
			for (Property property : uniquePropertyMap.getPropertiesByType(type)) {
				NodeProperty nodeProperty = (NodeProperty) property;
				int startPosition = nodeProperty.getStartPosition() - 1; // Adjust so it starts at 0
				int endPosition   = nodeProperty.getEndPosition() - 1;   // Adjust so it starts at 0
				
				for (int i = startPosition; i < endPosition + 1; i++) {
					String prevType = getSequenceLocations().get(i);
					if (prevType == null) {
						getSequenceLocations().put(i, type);
						
					} else {
						if (startPosition == endPosition) {
							getSequenceLocations().put(i, prioritiseType(prevType, type));
						}
					}
				}
			}
		}
	}
	
	private String prioritiseType(String prevType, String currType) {
		
		// This means the both types are either interval OR non-interval
		if (getOracle().getPropertyTable().getIntervalFlag(prevType) == getOracle().getPropertyTable().getIntervalFlag(currType)) {
			
			// This means both types are either list or non-list
			if (getOracle().getPropertyTable().getListFlag(prevType) && !getOracle().getPropertyTable().getListFlag(currType)) {
				// In which case, we don't care which one we return...so lets just return the current one
				return currType;			
			}
			// If the types are not the same, then we return whichever type is LIST
			return (getOracle().getPropertyTable().getListFlag(prevType)) ? prevType : currType;

		}
		// If the types are not the same, then we return whichever type is NON-INTERVAL
		return (getOracle().getPropertyTable().getIntervalFlag(prevType)) ? currType : prevType;
	}

	private void formatSequence(String sequence) {
		DefaultStyledDocument doc = new DefaultStyledDocument();
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrs, "monospaced");
		StyleConstants.setFontSize(attrs, 14);

		try {
			for (int i = 0; i < sequence.length(); i++) {
				// If there is a property in the sequence position, assign it to its designated color
				// Otherwise assign it to BLACK
				if (getSequenceLocations().containsKey(i)) {
					String type = getSequenceLocations().get(i);
					Color propertyColor = getOracle().getPropertyTable().getPropertyColour(type);
					StyleConstants.setBackground(attrs, propertyColor);
					StyleConstants.setForeground(attrs, Color.WHITE);
					StyleConstants.setBold(attrs, true);
				} else {
					StyleConstants.setBackground(attrs, Color.WHITE);
					StyleConstants.setForeground(attrs, Color.BLACK);
					StyleConstants.setBold(attrs, false);
				}
				doc.insertString(doc.getLength(), String.valueOf(sequence.charAt(i)), attrs);
			}
			setDocument(doc);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}