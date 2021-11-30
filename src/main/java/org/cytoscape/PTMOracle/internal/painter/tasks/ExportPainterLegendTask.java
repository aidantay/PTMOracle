package org.cytoscape.PTMOracle.internal.painter.tasks;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.io.LegendWriter;
import org.cytoscape.PTMOracle.internal.io.write.LegendPNGWriterImpl;
import org.cytoscape.PTMOracle.internal.model.ColorScheme;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * Task for writing painter properties to file. 
 * @author aidan
 */
public class ExportPainterLegendTask extends AbstractTask {

	@Tunable(description="Select file:", params="fileCategory=unspecified;input=false")
	public File file;

	private ColorScheme colorScheme;
	private BufferedImage image;
	
	public ExportPainterLegendTask(ColorScheme colorScheme) {
		super();
		
		this.colorScheme = colorScheme;
		this.image = new BufferedImage(450, 370, BufferedImage.TYPE_INT_ARGB);
	}

	public File getFile() {
		return file;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public ColorScheme getColorScheme() { 
		return colorScheme;
	}
	
	@ProvidesTitle
	public String getMenuTitle() {
		return "Export";
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Writing OraclePainter color scheme");
		taskMonitor.setStatusMessage("Exporting OraclePainter legend");

		createLegendImage();
		
		LegendWriter writer = new LegendPNGWriterImpl(getFile(), getImage()); 
		writer.run(taskMonitor);
	}
	
	private void createLegendImage() {
        Graphics2D g = getImage().createGraphics();
        
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, 500, 400);
        
		int x = 20;
		int y = 30;
		
		for (Pair<String, String> pair : getColorScheme().getSortedValues()) {
	    	String attribute = pair.getLeft();
	    	String value     = pair.getRight();
			Color c          = getColorScheme().getColor(pair);

			if (attribute.isEmpty()) {
				g.setColor(Color.BLACK);
				g.drawString(value, x, y);
	            x = x + 150;
	            
			} else {
				// Limit the length of the attribute and value to 25 characters
				attribute = attribute.length()  < 25 ? pair.getLeft()  : pair.getLeft().substring(0, 25).concat("...");
				value     = pair.getRight().length() < 25 ? pair.getRight() : pair.getRight().substring(0, 25).concat("..."); 
				
				g.setColor(Color.BLACK);
				g.drawString(attribute, x, y);
				
				x = x + 100;
				g.drawString(value, x, y);

				x = x + 200;
			}
			g.setColor(c);
			g.fillRect(x, y - 15, 50, 30);

			y = y + 35;
			x = 20;
		}
        g.dispose();	        
		g.drawImage(getImage(), 0, 0, null);
	}
	
}
