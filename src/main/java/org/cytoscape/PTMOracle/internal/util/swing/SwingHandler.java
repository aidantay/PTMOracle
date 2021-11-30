package org.cytoscape.PTMOracle.internal.util.swing;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;

import org.cytoscape.work.Tunable;
import org.cytoscape.work.swing.AbstractGUITunableHandler;

/**
 * Handler for creating custom Cytoscape Tunables. 
 * @author aidan
 */
public class SwingHandler<T extends JComponent> extends AbstractGUITunableHandler {
	
	private T swingComponent;
	
	public SwingHandler(Field f, Object o, Tunable t) {
		super(f, o, t);

		this.swingComponent = getComponent();
		this.swingComponent.setEnabled(true);

		init();
	}

	public SwingHandler(final Method getter, final Method setter, final Object instance, final Tunable tunable) {
		super(getter, setter, instance, tunable);
		init();
	}
	
	public T getSwingComponent() {
		return swingComponent;
	}

	public void init() {
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(getSwingComponent())
				.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
				)
		);
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING, true)
				.addComponent(getSwingComponent())
				.addGroup(layout.createSequentialGroup()
				)
		);
	}
	
	@SuppressWarnings("unchecked")
	private T getComponent() {
		try {
			return (T) getValue();
		} catch(final Exception e) {
			throw new NullPointerException("bad Swing object");	
		}
	}	
	
	// Not sure what I need to do for this
	@Override
	public void handle() {

	}

	
}
