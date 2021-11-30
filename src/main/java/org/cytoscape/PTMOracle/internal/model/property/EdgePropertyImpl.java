package org.cytoscape.PTMOracle.internal.model.property;

import org.cytoscape.PTMOracle.internal.model.EdgeProperty;

/**
 * Attributes/annotations on a EDGE.
 * Edge properties may include: Domain-Domain Interactions, Domain-Motif Interactions, etc...
 * @author aidan
 */
public class EdgePropertyImpl implements EdgeProperty {

	private String type;
	private String description;
	private String status;
	private String comments;
	
	public EdgePropertyImpl(String type, String description, 
			String status, String comments) {
		
		if (!containsValidParameters(type, description, status, comments)) {
			throw new IllegalArgumentException("Unable to create property:\t" 
												+ "TYPE:\t"        + type 
												+ "DESCRIPTION:\t" + description 
												+ "STATUS:\t"      + status  
												+ "COMMENTS:\t"    + comments);
		}
		
		setType(type);
		setDescription(description);
		setStatus(status);
		setComments(comments);
	}
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getDescription() {
		return description;
	}
		
	@Override
	public String getStatus() {
		return status;
	}
	
	@Override 
	public String getComments() {
		return comments;
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public void setStatus(String status) {
		this.status = status;	
	}

	@Override
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	private boolean containsValidParameters(String type, String description, 
			String status, String comments) {
		
		if (type == null || description == null || status == null || comments == null) {
			return false;
		}
		
		if (type.isEmpty() || description.isEmpty()) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return type + "\t" + description + status + "\t" + comments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgePropertyImpl other = (EdgePropertyImpl) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
