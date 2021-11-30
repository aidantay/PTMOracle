package org.cytoscape.PTMOracle.internal.schema;

import java.util.List;
import java.util.Set;

import org.cytoscape.PTMOracle.internal.model.PropertyMap;

/**
 * Table of edge properties that are imported from file.
 * Table stores:
 *  - ID (Primary Key)
 *  - Name of the file source (The file where the property is from)
 *  - Shared Edge Name (Shared name of the edge)
 *  - PropertyID (The ID of the property)
 *  - Type
 *  - Description
 *  - Source start position
 *  - Source end position
 *  - Target start position
 *  - Target end position
 *  - Status
 *  - Other comments
 * @author aidan
 */
public interface EdgePropertyTable extends OracleTable {

	public PropertyMap getProperties(List<String> compositeKey);
	public void deletePropertiesBySource(String source);
	public List<?> getCompositeKey(Integer id);
	public Set<String> getTypesBySource(String source);
	public Set<String> getAllSources();

}
