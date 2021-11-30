package org.cytoscape.PTMOracle.internal.painter.swing;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.cytoscape.PTMOracle.internal.painter.tasks.ApplyModPaintTask;
import org.cytoscape.PTMOracle.internal.painter.tasks.ApplyMultiPaintTask;
import org.cytoscape.PTMOracle.internal.painter.tasks.ClearPaintTask;
import org.cytoscape.PTMOracle.internal.painter.tasks.ExportPainterLegendTask;
import org.cytoscape.PTMOracle.internal.painter.tasks.ExportPainterPropertiesTask;
import org.cytoscape.PTMOracle.internal.painter.tasks.ImportPainterPropertiesTask;
import org.cytoscape.PTMOracle.internal.painter.tasks.PainterOptionsTask;
import org.cytoscape.PTMOracle.internal.util.swing.ScrollDisplay;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskIterator;

import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentVisualStyle;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentNetworkView;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentNetwork;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getTaskManager;

/**
 * Primary panel for the control panel.
 * TODO Individual painter preferences menu (analogous to the Cytoscape layouts).
 * @author aidan
 */
public class OraclePainterPanel extends JPanel implements CytoPanelComponent {
	
	private static final long serialVersionUID = -6677794379725565566L;
	private static final String MOD_PAINTER   = "ModPainter";
	private static final String MULTI_PAINTER = "MultiPainter";

	private JPanel topPanel;
	private JButton settingsButton;
	private JButton applyButton;
	private JButton clearButton;
	
	private JPopupMenu settingsMenu;
	private JMenuItem importStyle;
	private JMenuItem exportStyle;
	private JMenuItem createLegend;
	private JMenuItem painterOptions;
	private JMenuItem resetToDefault;
	private JMenuItem switchPainters;
	
	private JPanel painterPanel;
	private ModPainterPanel modPainterPanel;
	private MultiPainterPanel multiPainterPanel;
	
	private PainterOptionsTask painterOptionsTask;

	public OraclePainterPanel(CyNetwork network) {
		super(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());
		
		createTopPanel(network);
		createSettingsMenu();
		createPainterPanel(network);

		setMinimumSize(new Dimension(300, 400));
		setPreferredSize(new Dimension(300, 400));
		
		painterOptionsTask = new PainterOptionsTask();
		
		paint();
	}
	
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}
	
	public String getTitle() {
		return "OraclePainter";
	}
	
	public Component getComponent() {
		return this;
	}
	
	public Icon getIcon() {
		return null;
	}
	
	public ModPainterPanel getModPainterPanel() {
		return modPainterPanel;
	}
	
	public MultiPainterPanel getMultiPainterPanel() {
		return multiPainterPanel;
	}
	
	public void paint() {	
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		add(topPanel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		add(painterPanel, c);
	}

	private void createTopPanel(CyNetwork network) {
		topPanel = new JPanel(new GridBagLayout());
		
		settingsButton = new JButton("Settings");
		settingsButton.addActionListener(new SettingsButtonListener());
		
		applyButton = new JButton("Apply");
		applyButton.addActionListener(new ApplyListener());
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ClearButtonListener());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 10;
		c.weightx = 0.3;
		topPanel.add(settingsButton, c);
		
		c.gridx = 1;
		c.gridy = 0;
		topPanel.add(applyButton, c);
		
		c.gridx = 2;
		c.gridy = 0;
		topPanel.add(clearButton, c);
	}
	
	private void createSettingsMenu() {
		createLegend = new JMenuItem("Create legend");
		createLegend.addActionListener(new CreateLegendListener());
		
		importStyle = new JMenuItem("Import style");
		importStyle.addActionListener(new ImportStyleListener());
		
		exportStyle = new JMenuItem("Export style");
		exportStyle.addActionListener(new ExportStyleListener());
		
		painterOptions = new JMenuItem("Preferences");
		painterOptions.addActionListener(new PainterOptionsListener());
		
		resetToDefault = new JMenuItem("Reset to default");
		resetToDefault.addActionListener(new ResetToDefaultListener());
		
		switchPainters = new JMenuItem("Switch painter");
		switchPainters.addActionListener(new SwitchPainterListener());

		settingsMenu = new JPopupMenu();
		settingsMenu.add(createLegend);
		settingsMenu.add(importStyle);
		settingsMenu.add(exportStyle);
		settingsMenu.addSeparator();
		settingsMenu.add(painterOptions);
		settingsMenu.addSeparator();
		settingsMenu.add(resetToDefault);
		settingsMenu.add(switchPainters);
	}
	
	private void createPainterPanel(CyNetwork network) {
		painterPanel = new JPanel(new CardLayout());

		modPainterPanel = new ModPainterPanel();
		modPainterPanel.createColorPalette();					// Before we show the colors, create the palette

		multiPainterPanel = new MultiPainterPanel();
		multiPainterPanel.createColorPalette();					// Before we show the colors, create the palette
		
		painterPanel.add(new ScrollDisplay(modPainterPanel), MOD_PAINTER);
		painterPanel.add(new ScrollDisplay(multiPainterPanel), MULTI_PAINTER);
		
		if (network != null) {
			modPainterPanel.showSelectionPanel();			
			multiPainterPanel.showSelectionPanel();
		}
	}

	private class ApplyListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			if (getCurrentNetwork() != null) {
				// If the option is checked, clear the style we currently have.
				// Otherwise it will 'Add' the styles. This really only applies to the MultiPainter
				if (painterOptionsTask.getEnableRefreshFlag()) {
					getCurrentVisualStyle().apply(getCurrentNetworkView());
				}
				
				Task task = (modPainterPanel.isShowing()) ? 
						new ApplyModPaintTask(getCurrentNetwork(), painterOptionsTask.getPaintSelectedNodesFlag(), painterOptionsTask.getProportion(), modPainterPanel.getColorScheme()) :
						new ApplyMultiPaintTask(getCurrentNetwork(), painterOptionsTask.getPaintSelectedNodesFlag(), painterOptionsTask.getProportion(), multiPainterPanel.getColorScheme());

				getTaskManager().execute(new TaskIterator(task));
				getCurrentVisualStyle().apply(getCurrentNetworkView());
				getCurrentNetworkView().updateView();
			}
		}
	}
	
	private class ClearButtonListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			if (getCurrentNetworkView() != null) {
				Task task = new ClearPaintTask(getCurrentNetwork());
				getTaskManager().execute(new TaskIterator(task));
				
				getCurrentVisualStyle().apply(getCurrentNetworkView());
				getCurrentNetworkView().updateView();
			}
		}
	
	}
	
	private class SettingsButtonListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			settingsMenu.show(settingsButton, settingsButton.getWidth()/2, settingsButton.getHeight()/2);
		}
	
	}
	
	private class PainterOptionsListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			painterOptionsTask = new PainterOptionsTask();
			getTaskManager().execute(new TaskIterator(painterOptionsTask));
		}
	}

	private class SwitchPainterListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			// Theres only 2 choices. So this implementation works.
			CardLayout layout = (CardLayout) painterPanel.getLayout();
			layout.next(painterPanel);
		}
		
	}
	
	private class ResetToDefaultListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			modPainterPanel.showSelectionPanel();
		}
		
	}
	
	private class CreateLegendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Task task = (modPainterPanel.isShowing()) ? 
					new ExportPainterLegendTask(modPainterPanel.getColorScheme()) : 
					new ExportPainterLegendTask(multiPainterPanel.getColorScheme());
					
			getTaskManager().execute(new TaskIterator(task));
		}
		
	}
	
	private class ImportStyleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Task importPainterProperties = new ImportPainterPropertiesTask(modPainterPanel, multiPainterPanel);
			getTaskManager().execute(new TaskIterator(importPainterProperties));
		}

	}

	private class ExportStyleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Task task = (modPainterPanel.isShowing()) ? 
					new ExportPainterPropertiesTask(modPainterPanel.getColorScheme()) :  
					new ExportPainterPropertiesTask(multiPainterPanel.getColorScheme());

			getTaskManager().execute(new TaskIterator(task));
		}
		
	}

}