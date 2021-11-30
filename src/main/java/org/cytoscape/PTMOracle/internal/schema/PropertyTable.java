package org.cytoscape.PTMOracle.internal.schema;

import java.awt.Color;

/**
 * Table of acceptable properties. 
 * Users can add, remove or modify properties if necessary 
 * Table stores:
 *  - Property type (PrimaryKey)
 *  - Column name of property
 *  - Colour representation of the property used by OracleResults
 *  - Whether property is for nodes or edges
 *  - Whether property is interval
 *  - Whether property is mergeable 
 *  - Whether property is represented as a list or boolean 
 *  - Whether property is default. Default properties can ONLY be edited/modified
 * @author aidan
 */
public interface PropertyTable extends OracleTable {

	/*
	 * Node property types
	 */
	public static final String PTM      = "PTM";
	public static final String DOMAIN   = "Domain";
	public static final String DISORDER = "Disordered";
	public static final String MOTIF    = "Motif";
	public static final String SEQUENCE = "Sequence";
	
	public static final String PTM_COLUMN      = "UniqueModTypes";
	public static final String SEQUENCE_COLUMN = "HasSequence";
	public static final String DOMAIN_COLUMN   = "UniqueDomains";
	public static final String MOTIF_COLUMN    = "UniqueMotifs";
	public static final String DISORDER_COLUMN = "HasDisorderRegions";
	public static final String INT_RES_COLUMN  = "HasInteractingResidues";

	/*
	 * Edge property types
	 */
	public static final String DDI     = "DDI";
	public static final String DMI     = "DMI";
	public static final String INT_RES = "Interacting Residues";

	public static final String DDI_COLUMN = "HasDDI";
	public static final String DMI_COLUMN = "HasDMI";
	
	public String getPropertyColumnName(String type);
	public Color getPropertyColour(String type);
	public boolean getNodePropertyFlag(String type);
	public boolean getIntervalFlag(String type);
	public boolean getMergeableFlag(String type);
	public boolean getListFlag(String type);
	public boolean getDefaultFlag(String type);

}
