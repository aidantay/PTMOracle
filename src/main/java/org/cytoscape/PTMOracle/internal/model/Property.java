package org.cytoscape.PTMOracle.internal.model;

/**
 * Property are individual attributes/annotations on network components, such as NODES or EDGES. 
 * Node properties may include: PTMs, Domains, Motifs, etc...
 * Edge properties may include: Domain-Domain Interactions, Domain-Motif Interactions, etc...
 * @author aidan
 */
public interface Property {
	
	public String getType();
	public String getDescription();
	public String getStatus();
	public String getComments();

	public void setType(String type);
	public void setDescription(String description);
	public void setStatus(String status);
	public void setComments(String comments);
}
