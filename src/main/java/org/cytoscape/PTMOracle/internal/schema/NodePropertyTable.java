package org.cytoscape.PTMOracle.internal.schema;

import java.util.List;
import java.util.Set;

import org.cytoscape.PTMOracle.internal.model.PropertyMap;

/**
 * Table of node properties that are imported from file 
 * Table stores:
 *  - ID (Primary Key)
 *  - Name of the file source (The file where the property is from)
 *  - Shared Node Name (Shared name of the node)
 *  - PropertyID (The ID of the property)
 *  - Type
 *  - Description
 *  - Start position
 *  - End position
 *  - Residue
 *  - Status
 *  - Other comments
 * @author aidan
 */
public interface NodePropertyTable extends OracleTable {

	public PropertyMap getProperties(List<String> compositeKey);
	public void deletePropertiesBySource(String source);
	public List<?> getCompositeKey(Integer id);
	public Set<String> getTypesBySource(String source);
	public Set<String> getAllSources();
	
}
