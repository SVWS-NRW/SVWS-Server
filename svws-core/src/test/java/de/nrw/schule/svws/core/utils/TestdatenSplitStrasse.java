package de.nrw.schule.svws.core.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO-Klasse für das einlesen von Testdaten aus
 * einer CSV-Resource für das Testen des Aufteilens 
 * von Strassennamen.
 */
public class TestdatenSplitStrasse {

	/** Die Strasseninformation als ein kombinierter String */
	@JsonProperty
	public String strasse;

	/** Der Namensteil der Strasseninformation */
	@JsonProperty
	public String name;

	/** Der Teil mit der Hausnummer der Strasseninformation */
	@JsonProperty
	public String hausNr;
	
	/** Der Teil mit dem Hausnummerzusatz der Strasseninformation */
	@JsonProperty
	public String zusatz;
	
}
