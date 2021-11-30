package org.cytoscape.PTMOracle.internal.model;

/**
 * Attributes/annotations on a NODE. 
 * Node properties may include: PTMs, Domains, Motifs, etc...
 * @author aidan
 */
public interface NodeProperty extends Property {
	
	public static final String EMPTY_RESIDUE = "-";
	
	public int getStartPosition();
	public int getEndPosition();
	public String getResidue();
	
	public void setStartPosition(int startPosition);
	public void setEndPosition(int endPosition);
	public void setResidue(String residue);
	
}
