package org.cytoscape.PTMOracle.internal.results.swing;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cytoscape.PTMOracle.internal.results.tasks.CreateResultsSummaryTask;
import org.cytoscape.PTMOracle.internal.results.tasks.RefreshResultsTask;
import org.cytoscape.PTMOracle.internal.results.tasks.ResultsOptionsTask;
import org.cytoscape.PTMOracle.internal.util.swing.ScrollDisplay;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.work.TaskIterator;

import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getTaskManager;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentNetwork;

/**
 * Primary panel for the results panel. 
 * Additional panels are inserted here as individual tabs.
 * @author aidan
 */
public class OracleResultsPanel extends JPanel implements CytoPanelComponent {
		
	private static final long serialVersionUID = -6058780922250253549L;

	private static final String REFRESH_ICON = "refresh.png";
	
	private static final String NONE_LEGEND = "None";	
	private static final String NODE_LEGEND = "Node";
	private static final String EDGE_LEGEND = "Edge";
	
	private JPanel queryPanel;
	private JTextField queryField;
	private JButton queryButton;
	private JButton refreshButton;
	private JButton settingsButton;
	
	private JPopupMenu settingsMenu;
	private JMenuItem resultsOptions;
	private JMenuItem legendOption;
	
	private JPanel legendPanel;
	private JTabbedPane tabbedPane;
	
	private ResultsOptionsTask resultsOptionsTask;
	
	public OracleResultsPanel() {
		super(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());

		createQueryPanel();
		createSettingsMenu();
		createLegendPanel();

		resultsOptionsTask = new ResultsOptionsTask();	// Holds OracleResults Parameters
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setBorder(BorderFactory.createRaisedBevelBorder());
		tabbedPane.addChangeListener(new ChangeTabListener());
		
		paint();
	}
	
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}

	public String getTitle() {
		return "OracleResults";
	}

	public Component getComponent() {
		return this;
	}
	
	public Icon getIcon() {
		return null;
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
		
	// Get the refresh icon and scale it to an appropriate size.
	// Code taken from here: http://www.nullpointer.at/2011/08/21/java-code-snippets-howto-resize-an-imageicon/
	private ImageIcon getRefreshIcon() {
		ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/" + REFRESH_ICON));
		Image scaledImage = refreshIcon.getImage().getScaledInstance(25,  25,  Image.SCALE_SMOOTH);
		ImageIcon scaledRefreshIcon = new ImageIcon(scaledImage);
		return scaledRefreshIcon;
	}

	public void paint() {
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		add(queryPanel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0.8;
		add(tabbedPane, c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		c.weighty = 0.2;
		add(legendPanel, c);
	}
	
	private void createQueryPanel() {
		queryPanel = new JPanel(new GridBagLayout());
		queryPanel.setBorder(BorderFactory.createTitledBorder("Enter on protein ID/s"));
		
		settingsButton = new JButton("Settings");
		settingsButton.addActionListener(new SettingsButtonListener());

		queryField = new JTextField();
		queryButton = new JButton("View");
		queryButton.addActionListener(new ViewListener());
		
		refreshButton = new JButton(getRefreshIcon());
		refreshButton.addActionListener(new RefreshListener());
		
		GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.BOTH;
	    c.insets = new Insets(5, 5, 5, 5);
	    c.gridx = 0;
	    c.gridy = 0;
	    c.weightx = 1;
	    c.weighty = 1;
	    c.gridwidth = 4;
        queryPanel.add(queryField, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
	    c.weighty = 0;
	    c.gridwidth = 1;
        queryPanel.add(settingsButton, c);

        c.gridx = 1;
        c.gridy = 1;
	    c.weightx = 1;
        queryPanel.add(new JPanel(), c);
        
        c.gridx = 2;
        c.gridy = 1;
	    c.weightx = 0;
        queryPanel.add(queryButton, c);

		c.gridx = 3;
		c.gridy = 1;
		queryPanel.add(refreshButton, c);
	}
	
	private void createSettingsMenu() {
		resultsOptions = new JMenuItem("Preferences");
		resultsOptions.addActionListener(new ResultsOptionsListener());
		
		legendOption = new JMenuItem("Show Legend");
		legendOption.addActionListener(new LegendOptionListener());

		settingsMenu = new JPopupMenu();
		settingsMenu.add(resultsOptions);
		settingsMenu.addSeparator();
		settingsMenu.add(legendOption);
	}
	
	private void createLegendPanel() {
		legendPanel = new JPanel(new CardLayout());
		legendPanel.setBorder(BorderFactory.createTitledBorder("Legend"));

		legendPanel.add(new JPanel(), NONE_LEGEND);
		legendPanel.add(new ScrollDisplay(new ResultsLegendTableDisplay(true)), NODE_LEGEND);
		legendPanel.add(new ScrollDisplay(new ResultsLegendTableDisplay(false)), EDGE_LEGEND);
		legendPanel.setVisible(false);
	}

	private class ViewListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (getCurrentNetwork() != null) {
				String delimiter        = resultsOptionsTask.getDelimiter();
				String searchColumnName = resultsOptionsTask.getSearchColumnName();
				List<String> queryNames = Arrays.asList(queryField.getText().split(delimiter));
				CreateResultsSummaryTask task = new CreateResultsSummaryTask(getCurrentNetwork(), tabbedPane, queryNames, searchColumnName);
				getTaskManager().execute(new TaskIterator(task));
			}
		}
	}
	
	private class ChangeTabListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			CardLayout layout = (CardLayout) legendPanel.getLayout();
			
			if (tabbedPane.getTabCount() == 0) {
				layout.show(legendPanel, NONE_LEGEND);
	
			} else {
				ResultsSummaryPanel summaryPanel = (ResultsSummaryPanel) tabbedPane.getSelectedComponent();
				if (summaryPanel.isNodeResults()) {
					layout.show(legendPanel, NODE_LEGEND);
						
				} else {
					layout.show(legendPanel, EDGE_LEGEND);
				}
			}
		}
	}

	private class SettingsButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			settingsMenu.show(settingsButton, settingsButton.getWidth()/2, settingsButton.getHeight()/2);
		}
	
	}

	private class ResultsOptionsListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			resultsOptionsTask = new ResultsOptionsTask();
			getTaskManager().execute(new TaskIterator(resultsOptionsTask));
		}
	}
	
	private class LegendOptionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (legendPanel.isShowing()) {
				legendOption.setText("Show Legend");
				legendPanel.setVisible(false);
				
			} else {
				legendOption.setText("Hide Legend");
				legendPanel.setVisible(true);
			}
		}
	}

	private class RefreshListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			RefreshResultsTask task = new RefreshResultsTask(tabbedPane);
			getTaskManager().execute(new TaskIterator(task));
			
			// Check whether the legend is currently being shown or not
			// and check which card is shown
			boolean wasShowing = legendPanel.isShowing();
			String cardName;
			if (tabbedPane.getTabCount() == 0) {
				cardName = NONE_LEGEND;
			} else {
				ResultsSummaryPanel summaryPanel = (ResultsSummaryPanel) tabbedPane.getSelectedComponent();
				if (summaryPanel.isNodeResults()) {
					cardName = NODE_LEGEND;
				} else {
					cardName = EDGE_LEGEND;
				}
			}

			remove(legendPanel);
			createLegendPanel();
			((CardLayout) legendPanel.getLayout()).show(legendPanel, cardName);

			GridBagConstraints c = new GridBagConstraints();
		    c.fill = GridBagConstraints.BOTH;
		    c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 0;
			c.weighty = 0.2;
			add(legendPanel, c);
			repaint();
			revalidate();

			// If the legend was being shown before, then we should make it shown again
			if (wasShowing) {
				legendPanel.setVisible(true);
			}
		}
	}

}
