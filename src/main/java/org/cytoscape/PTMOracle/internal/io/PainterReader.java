package org.cytoscape.PTMOracle.internal.io;

import java.awt.Color;
import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.work.Task;

/**
 * PainterReaders are individual readers that parses painter parameters for NODEs from an input file
 * @author aidan
 */
public interface PainterReader extends Task, PainterIOUtil {
	
	public File getFile();
	public Map<Pair<String, String>, Color> getColorScheme();

}
