package org.cytoscape.PTMOracle.internal.model.property;

import org.cytoscape.PTMOracle.internal.model.NodeProperty;

/**
 * Attributes/annotations on a NODE. 
 * Node properties may include: PTMs, Domains, Motifs, etc...
 * @author aidan
 */
public class NodePropertyImpl implements NodeProperty {

	private String type;
	private String description;
	private int startPosition;
	private int endPosition;
	private String residue;
	private String status;
	private String comments;
	
	public NodePropertyImpl(String type, String description, 
			int startPosition, int endPosition, String residue, 
			String status, String comments) {

		if (!containsValidParameters(type, description, startPosition, endPosition, residue, status, comments)) {
			throw new IllegalArgumentException("Unable to create property:\t" 
												+ "TYPE:\t"        + type 
												+ "DESCRIPTION:\t" + description 
												+ "START_POS:\t"   + startPosition
												+ "END_POS:\t"     + endPosition  
												+ "RESIDUE:\t"     + residue  
												+ "STATUS:\t"      + status  
												+ "COMMENTS:\t"    + comments);
		}
		
		setType(type);
		setDescription(description);
		setStartPosition(startPosition);
		setEndPosition(endPosition);
		setResidue(residue);		
		setStatus(status);
		setComments(comments);
	}
	
	public NodePropertyImpl(String type, String description,
			int startPosition, int endPosition, String residue) {
		
		this(type, description, startPosition, endPosition, residue, "", "");
	}

	public NodePropertyImpl(String type, String description,
			int startPosition, int EndPosition) {
		
		this(type, description, startPosition, EndPosition, EMPTY_RESIDUE);
	}
	
	public NodePropertyImpl(String type,  
			int startPosition, int endPosition, String residue, 
			String status, String comments) {

		this(type, null, startPosition, endPosition, residue, status, comments);
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
	public int getStartPosition() {
		return startPosition;
	}
	
	@Override
	public int getEndPosition() {
		return endPosition;
	}
	
	@Override
	public String getResidue() {
		return residue;
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
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	
	@Override
	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}
	
	@Override
	public void setResidue(String residue) {
		this.residue = residue.toUpperCase();
	}
	
	@Override
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public void setComments(String comments) {
		this.comments = comments;
	}

	// Checks for determining whether the parameters we have for a NODE PROPERTY are actually valid
	private boolean containsValidParameters(String type, String description, 
			int startPosition, int endPosition, String residue, 
			String status, String comments) {
		
		// Positions must not be negative. Also, the start must not be greater than the end  
		if (startPosition <= 0 || endPosition <= 0 || startPosition > endPosition) {
			return false;
		}
		
		// Residue must not be null, empty or contain more than 1 character. Also, residue must be a character or '-'
		if (residue == null || !residue.matches("^[A-Za-z-]$")) {
			return false;
		}
		
		// Type and description must not be null or empty
		if (type == null || type.isEmpty() || description == null || description.isEmpty()) {
			return false;
		}
		
		// Status and comments must not be null (They can be empty)
		if (status == null || comments == null) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		
		out.append(type + "\t" + startPosition + "\t" + endPosition + "\t");
		out.append(residue + "\t" + description + "\t" + status + "\t" + comments);
		
		return out.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + endPosition;
		result = prime * result + startPosition;
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
		NodePropertyImpl other = (NodePropertyImpl) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (endPosition != other.endPosition)
			return false;
		if (startPosition != other.startPosition)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	
}
