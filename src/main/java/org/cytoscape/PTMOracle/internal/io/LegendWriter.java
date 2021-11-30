package org.cytoscape.PTMOracle.internal.io;

import java.awt.image.BufferedImage;
import java.io.File;

import org.cytoscape.work.Task;

/**
 * LegendWriters are individual writers that composes the color scheme for NODES into an output file 
 * @author aidan
 */
public interface LegendWriter extends Task {
	
	public File getFile();
	public BufferedImage getColorSchemeImage();
}
