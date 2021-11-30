package org.cytoscape.PTMOracle.internal.io;

import java.io.File;

import org.cytoscape.work.Task;

/**
 * PropertyWriters are individual writers that composes properties for NODES and EDGES into an output file
 * @author aidan
 */
public interface PropertyWriter extends Task, PropertyIOUtil {

	public File getFile();

}
