package org.cytoscape.PTMOracle.internal.preferences.swing;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cytoscape.PTMOracle.internal.preferences.tasks.AddKeywordRowTask;
import org.cytoscape.PTMOracle.internal.preferences.tasks.AddNetworkMappingRowTask;
import org.cytoscape.PTMOracle.internal.preferences.tasks.AddPropertyRowTask;
import org.cytoscape.PTMOracle.internal.schema.swing.EdgePropertyTableDisplay;
import org.cytoscape.PTMOracle.internal.schema.swing.KeywordTableDisplay;
import org.cytoscape.PTMOracle.internal.schema.swing.NetworkMappingTableDisplay;
import org.cytoscape.PTMOracle.internal.schema.swing.NodePropertyTableDisplay;
import org.cytoscape.PTMOracle.internal.schema.swing.PropertyTableDisplay;
import org.cytoscape.PTMOracle.internal.schema.swing.SchemaTableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.ListDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.ScrollDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskIterator;

import static org.cytoscape.PTMOracle.internal.preferences.swing.PreferencesListDisplay.*;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getTaskManager;

/**
 * Primary panel for displaying preferences/settings. 
 * Additional panels are inserted here as individual panels into the SCROLLPANE.
 * @author aidan
 */
public class PreferencesPanel extends JPanel {

	private static final long serialVersionUID = -8454610429405691132L;
	
	private JPanel preferenceListPanel;
	private ListDisplay preferenceList;
	
	private JPanel viewPanel;
	private Map<String, TableDisplay> tableDisplayMap;
	
	private JPanel buttonPanel;
	private JButton addButton;
	private JButton removeButton;
	
	public PreferencesPanel() {
		super(new GridBagLayout());
		
		createTables();
		createButtonPanel();
		createViewPanel();
		createPreferenceListPanel();
				
		paint();
	}
	
	public void paint() {
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		add(preferenceListPanel, c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		add(viewPanel, c);

		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		add(buttonPanel, c);
	}
	
	// Ordering is important!
	public List<TableDisplay> getTableDisplayList() {
		List<TableDisplay> tableDisplayList = new ArrayList<TableDisplay>();
		tableDisplayList.add(getTableDisplay(PROPERTY));
		tableDisplayList.add(getTableDisplay(KEYWORD));
		tableDisplayList.add(getTableDisplay(MAPPING));
		tableDisplayList.add(getTableDisplay(ORACLE_SCHEMA));
		tableDisplayList.add(getTableDisplay(NODE));
		tableDisplayList.add(getTableDisplay(EDGE));
		return tableDisplayList;
	}

	private TableDisplay getTableDisplay(String tableDisplayName) {
		return tableDisplayMap.get(tableDisplayName);
	}
	
	private void createTables() {
		tableDisplayMap = new HashMap<String, TableDisplay>();
		tableDisplayMap.put(PROPERTY, new PropertyTableDisplay());
		tableDisplayMap.put(KEYWORD, new KeywordTableDisplay());
		tableDisplayMap.put(MAPPING, new NetworkMappingTableDisplay());
		tableDisplayMap.put(ORACLE_SCHEMA, new SchemaTableDisplay());
		tableDisplayMap.put(NODE, new NodePropertyTableDisplay());
		tableDisplayMap.put(EDGE, new EdgePropertyTableDisplay());
	}
	
	private void createViewPanel() {		
		viewPanel = new JPanel(new CardLayout());
		viewPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		viewPanel.add(new ScrollDisplay(getTableDisplay(PROPERTY)), PROPERTY);
		viewPanel.add(new ScrollDisplay(getTableDisplay(KEYWORD)), KEYWORD);
		viewPanel.add(new ScrollDisplay(getTableDisplay(MAPPING)), MAPPING);
		viewPanel.add(new ScrollDisplay(getTableDisplay(ORACLE_SCHEMA)), ORACLE_SCHEMA);
		viewPanel.add(new ScrollDisplay(getTableDisplay(NODE)), NODE);
		viewPanel.add(new ScrollDisplay(getTableDisplay(EDGE)), EDGE);
	}
		
	private void createPreferenceListPanel() {
		preferenceListPanel = new JPanel(new GridBagLayout());
		
		preferenceList = new PreferencesListDisplay();
		preferenceList.addListSelectionListener(new PreferenceListSelectionListener());
		preferenceList.setSelectedIndex(0);

		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        c.weighty = 1;
        preferenceListPanel.add(new ScrollDisplay(preferenceList), c);
	}
	
	private void createButtonPanel() {
		buttonPanel = new JPanel(new GridBagLayout());

		addButton = new JButton("Add");
		addButton.addActionListener(new AddRowListener());
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new RemoveRowListener());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        buttonPanel.add(addButton, c);

        c.gridx = 0;
        c.gridy = 2;
		buttonPanel.add(removeButton, c);
	}
	
	private class PreferenceListSelectionListener implements ListSelectionListener {

		@Override
		@SuppressWarnings("rawtypes")
		public void valueChanged(ListSelectionEvent e) {
			JList list = (JList) e.getSource();
			String value = (String) list.getSelectedValue();

			CardLayout layout = (CardLayout) viewPanel.getLayout();
			layout.show(viewPanel, value);

			if (value.equals(PROPERTY) || value.equals(KEYWORD) || value.equals(MAPPING)) {
				buttonPanel.setVisible(true);
				
			} else {
				buttonPanel.setVisible(false);
			}
		}
	}

	private class AddRowListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String value = preferenceList.getSelectedValue();
			TableDisplay table = getTableDisplay(preferenceList.getSelectedValue());
					
			if (value.equals(PROPERTY)) {
				Task addPropertyRowTask = new AddPropertyRowTask(table);
				getTaskManager().execute(new TaskIterator(addPropertyRowTask));

			} else if (value.equals(KEYWORD)) {
				Task addKeywordRowTask = new AddKeywordRowTask(table);
				getTaskManager().execute(new TaskIterator(addKeywordRowTask));
				
			} else if (value.equals(MAPPING)) {
				Task addNetworkMappingTask = new AddNetworkMappingRowTask(table);
				getTaskManager().execute(new TaskIterator(addNetworkMappingTask));
			}
			
		}
	}
	
	private class RemoveRowListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			TableDisplay table = getTableDisplay(preferenceList.getSelectedValue());
			if (table != null) {
				for (int selectedRowIndex : table.getSelectedRows()) {
					Class<?> columnClass = table.getColumnClass(table.getColumnCount() - 1);
					if (columnClass.equals(Boolean.class)) {
						boolean isDefault = (Boolean) table.getValueAt(selectedRowIndex, table.getColumnCount() - 1);			
						if (!isDefault) {
							int rowIndex = table.convertRowIndexToModel(selectedRowIndex);
							table.getModel().removeRow(rowIndex);
						} else {
							JOptionPane.showMessageDialog(PreferencesPanel.this, "Default rows can only be edited. They cannot be removed!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						int rowIndex = table.convertRowIndexToModel(selectedRowIndex);
						table.getModel().removeRow(rowIndex);						
					}
				}
			}
		}
		
	}
}
