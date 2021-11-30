package org.cytoscape.PTMOracle.internal.io;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.model.PropertyCollection;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.work.Task;

/**
 * PropertyReaders are individual readers that parses properties for NODES and EDGES from an input file
 * @author aidan
 */
public interface PropertyReader extends Task, PropertyIOUtil {
	
	public File getFile();
	public PropertyCollection getInitialPropertyCollection();
	public PropertyCollection getResolvedPropertyCollection();
	public PropertyCollection getUnresolvedPropertyCollection();
	public Map<Pair<String, String>, List<String>> getPossibleKeywordMap();

	public void convertDescriptionsToKeyword();
	public void setUnresolvedProperties(TableDisplay tableDisplay);
	
}
