package org.cytoscape.PTMOracle.internal.io.write;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.cytoscape.PTMOracle.internal.io.LegendWriter;
import org.cytoscape.work.TaskMonitor;

/**
 * LegendPNGWriter composes the colour scheme for NODEs into an output PNG file  
 * @author aidan
 */
public class LegendPNGWriterImpl implements LegendWriter {

	public static final String PNG_FILE = "PNG";	

	private File file;
	private BufferedImage colorSchemeImage;
	
	public LegendPNGWriterImpl(File file, BufferedImage colorSchemeImage) {
		this.file = file;
		this.colorSchemeImage = colorSchemeImage;
	}
	
	@Override
	public File getFile() {
		return file;
	}
	
	@Override
	public BufferedImage getColorSchemeImage() {
		return colorSchemeImage;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		try {
			ImageIO.write(getColorSchemeImage(), PNG_FILE, getFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void cancel() {
		
	}

}
