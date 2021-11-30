package org.cytoscape.PTMOracle.internal.io;

import java.io.File;

import org.cytoscape.PTMOracle.internal.model.ColorScheme;
import org.cytoscape.work.Task;

/**
 * PainterWriters are individual writers that composes painter properties for NODES into an output file
 * @author aidan
 */
public interface PainterWriter extends Task, PainterIOUtil {
	
	public File getFile();
 	public ColorScheme getColorScheme();
 	
}
