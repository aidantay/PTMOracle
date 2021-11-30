package org.cytoscape.PTMOracle.internal.util.swing.impl;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * Class for wrapping text in a JTextPane. 
 * Code taken from here: https://community.oracle.com/thread/2376090
 * @author aidan
 */
public class WrapEditorKit extends StyledEditorKit {
	
 	private static final long serialVersionUID = 6485039410567746842L;
	private ViewFactory defaultFactory;
    
	public WrapEditorKit() {
		defaultFactory = new WrapColumnFactory();
	}
	
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    private class WrapColumnFactory implements ViewFactory {
    	public View create(Element elem) {
    		String kind = elem.getName();
    		if (kind != null) {
    			if (kind.equals(AbstractDocument.ContentElementName)) {
    				return new WrapLabelView(elem);
    			} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
    				return new ParagraphView(elem);
    			} else if (kind.equals(AbstractDocument.SectionElementName)) {
    				return new BoxView(elem, View.Y_AXIS);
    			} else if (kind.equals(StyleConstants.ComponentElementName)) {
    				return new ComponentView(elem);
    			} else if (kind.equals(StyleConstants.IconElementName)) {
    				return new IconView(elem);
    			}
    		}

            // default to text display
            return new LabelView(elem);
        }
    }

    private class WrapLabelView extends LabelView {
    	public WrapLabelView(Element elem) {
    		super(elem);
    	}

    	public float getMinimumSpan(int axis) {
    		switch (axis) {
	    		case View.X_AXIS:
	    			return 0;
	    		case View.Y_AXIS:
	    			return super.getMinimumSpan(axis);
	    		default:
	    			throw new IllegalArgumentException("Invalid axis: " + axis);
    		}
    	}
    }
    
}



