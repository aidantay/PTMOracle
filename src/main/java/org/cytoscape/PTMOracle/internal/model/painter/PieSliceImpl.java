package org.cytoscape.PTMOracle.internal.model.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import org.cytoscape.PTMOracle.internal.model.PieSlice;
import org.cytoscape.view.presentation.customgraphics.CustomGraphicLayer;

/**
 * PieSlice is the visual representation of an individual portion of a pie chart.
 * Code for this mostly taken from elsewhere (forgot where!).
 * @author aidan
 */
public class PieSliceImpl implements PieSlice {

	private static final float BORDER_WIDTH = (float) 2.0;			// Divisions in pie chart go off sometimes because of the line thickness... Not sure if this can actually be fixed? 
	
	private double arcStart;
	private double arcLength;
	private Rectangle2D bounds;
	private Paint sliceColor;
	
	public PieSliceImpl(double arcStart, double arcLength, Color sliceColor) {
		this.arcStart = arcStart;
		this.arcLength = arcLength;
		this.sliceColor = sliceColor;
		this.bounds = new Rectangle2D.Double(0, 0, 100, 100);
	}
	
	@Override
	public double getArcStart() {
		return arcStart;
	}
	
	@Override
	public double getArcLength() {
		return arcLength;
	}
	
	@Override
	public Rectangle2D getBounds2D() {
		return bounds;
	}

	@Override
	// This function is not used!
	public Paint getPaint(Rectangle2D bounds) {
		return sliceColor;
	}

	@Override
	// Transforms the bounds such that it fits within the node shape
	public CustomGraphicLayer transform(AffineTransform xform) {
		Shape newBounds = xform.createTransformedShape(bounds);
		this.bounds = newBounds.getBounds2D();
		return this;
	}

	@Override
	public Shape getShape() {
		double x = getBounds2D().getX()-getBounds2D().getWidth()/2;
		double y = getBounds2D().getY()-getBounds2D().getHeight()/2;
		double width = getBounds2D().getWidth();
		double height = getBounds2D().getHeight();
		Arc2D slice = new Arc2D.Double(x, y, width, height, getArcStart(), getArcLength(), Arc2D.PIE);
		return slice;
	}

	@Override
	public Paint getPaint() {
		return sliceColor;
	}

	@Override
	public Stroke getStroke() {
		return new BasicStroke(BORDER_WIDTH);
	}

	@Override
	public Paint getStrokePaint() {
		return Color.BLACK;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		
		out.append(arcStart + "\t" + arcLength);
		
		return out.toString();
	}
	
}
