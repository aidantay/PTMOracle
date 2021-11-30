package org.cytoscape.PTMOracle.internal.schema;

import java.util.List;
import java.util.Set;

/**
 * Table of network-source pairings.
 * These pairings map the root network to the imported oracle files.
 * Table stores: 
 *  - ID (Primary Key)
 *  - Name of the root network (Composite Key)
 *  - Name of the file source (Composite Key)
 * @author aidan
 */
public interface NetworkMappingTable extends OracleTable {

	public List<?> getCompositeKey(Integer id);
	public Set<String> getSources(String rootNetworkName);
	public Set<String> getRootNetworkNames(String source);
	
}
