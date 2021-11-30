package org.cytoscape.PTMOracle.internal.schema;

import java.util.List;

/**
 * Table of keyword-regex pairings. 
 * Users can add, remove or modify pairings if necessary.
 * These pairings will map property descriptions to a common keyword based on the regex.
 * Table stores: 
 *  - ID (Primary Key)
 *  - Common keyword [eg. Phosphorylation] (Composite Key)
 *  - Property type [eg. PTM] (Composite Key)
 *  - Colour representation of the keyword used by OraclePainter (only for PTMs).  
 *  - Regular expression that matches a property description
 *  - Whether pairing is default. Default pairings can ONLY be edited/modified
 * @author aidan
 */
public interface KeywordTable extends OracleTable {

	/*
	 * Keywords
	 */
	public static final String PHOSPHORYLATION        = "Phosphorylation";
	public static final String METHYLATION            = "Methylation";
	public static final String ACETYLATION            = "Acetylation";
	public static final String N_LINKED_GLYCOSYLATION = "N-linked Glycosylation";
	public static final String O_LINKED_GLYCOSYLATION = "O-linked Glycosylation";
	public static final String LIPIDATION             = "Lipidation";
	public static final String UBIQUITINATION         = "Ubiquitination";
	public static final String SUMOYLATION            = "Sumoylation";
	
	/*
	 * Regex
	 */
	public static final String PHOSPHORYLATION_REGEX        = "[Pp]hospho";
	public static final String METHYLATION_REGEX            = "[Mm]ethyl";
	public static final String ACETYLATION_REGEX            = "[Aa]cetyl";
	public static final String N_LINKED_GLYCOSYLATION_REGEX = "[Nn]-linked";
	public static final String O_LINKED_GLYCOSYLATION_REGEX = "[Oo]-linked";
	public static final String LIPIDATION_REGEX             = "[Pp]al|[Gg]er|[Ff]arn|[Mm]yris|[Ll]ipo";
	public static final String UBIQUITINATION_REGEX         = "[Uu]biquit[iy]";
	public static final String SUMOYLATION_REGEX            = "[Ss][Uu][Mm][Oo][Yy][Ll]";
	public static final String ANY_REGEX                    = ".*";
	
	public static final String NOT_APPLICABLE               = "N/A";
	
	public List<String> convertDescriptionToKeyword(String description, String type);
	public List<?> getCompositeKey(Integer id);

	public Object getColor(List<?> compositeKey);
	public String getRegex(List<?> compositeKey);
}
