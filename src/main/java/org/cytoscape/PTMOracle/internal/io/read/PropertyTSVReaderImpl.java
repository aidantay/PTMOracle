package org.cytoscape.PTMOracle.internal.io.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.cytoscape.PTMOracle.internal.io.AbstractPropertyReader;
import org.cytoscape.PTMOracle.internal.io.PropertyReader;
import org.cytoscape.PTMOracle.internal.model.EdgeProperty;
import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.property.EdgePropertyImpl;
import org.cytoscape.PTMOracle.internal.model.property.NodePropertyImpl;
import org.cytoscape.work.TaskMonitor;

/**
 * PropertyTSVReader parses properties for NODEs and EDGES in a TSV file
 * @author aidan
 */
public class PropertyTSVReaderImpl extends AbstractPropertyReader implements PropertyReader {

	public PropertyTSVReaderImpl(File file) {
		super(file);
	}
	
	public void run(TaskMonitor taskMonitor, List<Integer> indexList) throws Exception {
		BufferedReader fileReader = new BufferedReader(new FileReader(getFile()));
		String line;
			
		while ((line = fileReader.readLine()) != null) {
		   	String[] parts = line.split("\\t");
	   		parseLine(parts, indexList);
		}
		fileReader.close();
	}
		
	private void parseLine(String[] parts, List<Integer> indexList) {
		String accession   = parts[indexList.get(0)];
		String type        = parts[indexList.get(1)];
		String description = parts[indexList.get(2)];
		int startPos       = Integer.valueOf(parts[indexList.get(3)]);
		int endPos         = Integer.valueOf(parts[indexList.get(4)]);
		String residue     = (indexList.get(5) == -1) ? "-" : parts[indexList.get(5)];
		
		NodeProperty property = new NodePropertyImpl(type, description, startPos, endPos, residue);
		getInitialPropertyCollection().addProperty(accession, property);
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		BufferedReader fileReader = new BufferedReader(new FileReader(getFile()));
		String line;
			
		while ((line = fileReader.readLine()) != null) {
            if (line.matches("^\\s*$")) {
                continue;
            }
            
		   	String[] parts  = line.split("\\t");
		   	String property = parts[0];
		   	
		   	// If the first column contains the 'edge' tag, then we assume its an edge property
		   	if (property.equalsIgnoreCase(EDGE_LINE)) {
		   		parseEdge(parts);
		   		
		   	// Otherwise, check if it contains the 'node' tag 
		   	// This means we may or may not have a 'node' tag
		   	} else {
		   		parseNode(parts);
		   	}

		}
		fileReader.close();
	}
	
	private void parseNode(String[] parts) {
		String accession, type, description, residue, status, comments;
		int startPos, endPos;
		
		// Account for cases where there is a 'node' tag in the first column
		if (parts[0].equalsIgnoreCase(NODE_LINE)) {
			accession   = parts[1];
			type        = parts[2];
			description = parts[3];
			startPos    = Integer.valueOf(parts[4]);
			endPos      = Integer.valueOf(parts[5]);
			residue     = parts[6];
			status      = (parts.length <= 7) ? "" : parts[7];	// This is optional
			comments    = (parts.length <= 8) ? "" : parts[8];	// This is optional

		// Account for cases where there is no 'node' tag in the first column
		} else {
			accession   = parts[0];
			type        = parts[1];
			description = parts[2];
			startPos    = Integer.valueOf(parts[3]);
			endPos      = Integer.valueOf(parts[4]);
			residue     = parts[5];
			status      = (parts.length <= 6) ? "" : parts[6];	// This is optional
			comments    = (parts.length <= 7) ? "" : parts[7];	// This is optional
		}

		// We can't do much error checking we can do here since anything and everything is possible
		// Only thing that keeps us in check here is the Integer
		// In any case, invalid parameters will be caught when we try to create the property
		NodeProperty property = new NodePropertyImpl(type, description, startPos, endPos, residue, status, comments);
		getInitialPropertyCollection().addProperty(accession, property);
	}
	
	private void parseEdge(String[] parts) {
		String sourceId    = parts[1];
		String targetId    = parts[2];
		String type        = parts[3];
		String description = parts[4];
		String status      = (parts.length <= 5) ? "" : parts[5];	// This is optional
		String comments    = (parts.length <= 6) ? "" : parts[6];	// This is optional

		// We can't do much error checking we can do here since anything and everything is possible
		// Only thing that keeps us in check here is the Integer
		// In any case, invalid parameters will be caught when we try to create the property
		EdgeProperty property = new EdgePropertyImpl(type, description, status, comments);
		getInitialPropertyCollection().addProperty(sourceId, targetId, property);
	}
	
}
