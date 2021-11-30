package org.cytoscape.PTMOracle.internal.preferences.tasks;

import java.awt.Color;
import java.io.IOException;
import java.util.Vector;

import org.cytoscape.PTMOracle.internal.preferences.AbstractInsertTableRowTask;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * Task adding row into PropertyTableDisplay in the Preferences menu
 * @author 
 */
public class AddPropertyRowTask extends AbstractInsertTableRowTask {

	@Tunable(description="Property type", groups={"Required fields"})
	public String propertyType;
	
	@Tunable(description="Column name", groups={"Required fields"})
	public String columnName;
	
	@Tunable(description="Color code (Hex)", groups={"Required fields"})
	public String color;

	@Tunable(description="Is node property?", groups={"Other information"})
	public boolean isNodeProperty;
		
	@Tunable(description="Is interval?", groups={"Other information"})
	public boolean isInterval;

	@Tunable(description="Is mergeable?", groups={"Other information"})
	public boolean isMergeable;

	@Tunable(description="Is column list?", groups={"Other information"})
	public boolean isColumnList;
		
	public AddPropertyRowTask(TableDisplay table) {
		super(table);
		
		this.isNodeProperty = true;
		this.isColumnList   = true;
	}
		
	@ProvidesTitle
	public String getMenuTitle() {
		return "Property Attributes";
	}
	
	public String getPropertyType() {
		return propertyType;
	}

	public boolean getIsNodeProperty() {
		return isNodeProperty;
	}

	public String getColumnName() {
		return columnName;
	}
	
	public String getColor() {
		return color;
	}
	
	public boolean getIsInterval() {
		return isInterval;
	}
	
	public boolean getIsMergeable() {
		return isMergeable;
	}
	
	public boolean getIsColumnList() {
		return isColumnList;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public void run(TaskMonitor taskMonitor) throws Exception {
		Vector row = new Vector();		
		row.add(getPropertyType());
		row.add(getColumnName());
		row.add(Color.decode(getColor()));
		row.add(getIsNodeProperty());
		row.add(getIsInterval());
		row.add(getIsMergeable());
		row.add(getIsColumnList());
		row.add(false);		
		getTable().getModel().addRow(row);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public ValidationState getValidationState(Appendable errMsg) {
		try {			
			// Check that the type and column name are valid string inputs,
			// and that the color code is a valid hex number
			if (!isValidCellString(getPropertyType()) 
				|| !isValidCellString(getColumnName())
				|| !isValidHexCode(getColor())) {
				errMsg.append("Invalid table contents.\nPlease see manual.");
				return ValidationState.INVALID;
			}
			
			Vector updatedRows = (Vector) getTable().getModel().getDataVector();
			for (Object o : updatedRows) {
				Vector row = (Vector) o;
				String currType       = (String) row.get(0);
				String currColumnName = (String) row.get(1);
				
				// Check that the type or column name are unique values. (i.e. we do not have one currently in the table)
				if (currType.equals(getPropertyType()) 
					|| currColumnName.equals(getColumnName())) {
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