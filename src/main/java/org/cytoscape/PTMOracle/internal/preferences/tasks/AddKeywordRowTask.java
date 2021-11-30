package org.cytoscape.PTMOracle.internal.preferences.tasks;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.cytoscape.PTMOracle.internal.preferences.AbstractInsertTableRowTask;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.PropertyComboBoxDisplay;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.util.ListSingleSelection;

import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.PTM;
import static org.cytoscape.PTMOracle.internal.schema.KeywordTable.NOT_APPLICABLE;

/**
 * Task adding row into KeywordTableDisplay in the Preferences menu
 * @author 
 */
public class AddKeywordRowTask extends AbstractInsertTableRowTask {

	@Tunable(description="Keyword", groups={"Required fields"})
	public String keyword;
	
	@Tunable(description="Property Type", groups={"Required fields"})
	public ListSingleSelection<String> propertyType;
	
	@Tunable(description="Color code (Hex)", groups={"Required fields"}, dependsOn="propertyType=PTM")
	public String color;

	@Tunable(description="Regular expression pattern", groups={"Required fields"})
	public String regex;
	
	public AddKeywordRowTask(TableDisplay table) {
		super(table);
		
		this.propertyType = getPropertyTypes();
	}
	
	private ListSingleSelection<String> getPropertyTypes() {
		List<String> propertyTypes = new ArrayList<String>();
		
		for (String type : (new PropertyComboBoxDisplay()).getValues()) {
			propertyTypes.add(type);
		}
		return new ListSingleSelection<String>(propertyTypes);
	}
		
	@ProvidesTitle
	public String getMenuTitle() {
		return "Keyword Attributes";
	}
	
	public String getKeyword() {
		return keyword;
	}

	public String getPropertyType() {
		return propertyType.getSelectedValue();
	}
	
	public String getColor() {
		return color;
	}

	public String getRegex() {
		return regex;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	private Object convertColor() {
		try {
			return Color.decode(getColor());
			
		} catch(NumberFormatException e) {
			return getColor();
		}
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public void run(TaskMonitor taskMonitor) throws Exception {
		Vector row = new Vector();
		row.add(null);
		row.add(getKeyword());
		row.add(getPropertyType());
		row.add(convertColor());
		row.add(getRegex());
		getTable().getModel().addRow(row);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public ValidationState getValidationState(Appendable errMsg) {
		try {
			// Check that the type and column name are valid string inputs,
			if (!isValidCellString(getKeyword())
				|| !isValidCellString(getRegex())) {
				errMsg.append("Invalid table contents.\nPlease see manual.");
				return ValidationState.INVALID;
			}
			
			// Check whether we have a PTM or not
			if (getPropertyType().equals(PTM)) {
				// If we have a PTM, then check that the color code is a valid hex number
				if (!isValidHexCode(getColor())) {
					errMsg.append("Invalid table contents.\nPlease see manual.");
					return ValidationState.INVALID;
				}
			
			// If we don't have a PTM then just set the color to NOT_APPLICABLE
			} else {
				setColor(NOT_APPLICABLE);
			}
			
			Vector updatedRows = (Vector) getTable().getModel().getDataVector();
			for (Object o : updatedRows) {
				Vector row = (Vector) o;
				String currKeyword = (String) row.get(1);
				String currType    = (String) row.get(2);
				
				// Check that the (keyword, type) pair is unique (i.e. we do not have one currently in the table) 				
				if (currType.equals(getPropertyType())
					&& currKeyword.equals(getKeyword())) { 
					errMsg.append("Invalid table contents.\nPlease see manual.");
					return ValidationState.INVALID;					
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ValidationState.OK;
	}

}