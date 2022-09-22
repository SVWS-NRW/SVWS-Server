package de.nrw.schule.svws.csv.statkue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.csv.CsvReader;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_LehrerFachrichtungAnerkennung.
 */
@JsonPropertyOrder({"ID","Kurztext","Langtext","Beginn","Ende","Sort","gueltigVon","gueltigBis"})
public class DataStatkueLehrerFachrichtungAnerkennung {

	/** Statkue Tabelle IT.NRW: ID */
	@JsonProperty
	public Integer ID;

	/** Statkue Tabelle IT.NRW: Eine dreistellige Nummer - der Schlüssel der Amtlichen Schulstatistik für diesen Datensatz */
	@JsonProperty
	public String Kurztext;

	/** Statkue Tabelle IT.NRW: Bezeichnung der Anerkennung der Fachrichtung */
	@JsonProperty
	public String Langtext;

	/** Statkue Tabelle IT.NRW: ??? */
	@JsonProperty
	public String Beginn;

	/** Statkue Tabelle IT.NRW:  ??? */
	@JsonProperty
	public String Ende;

	/** Statkue Tabelle IT.NRW: Sortierung der Anerkennung der Fachrichtung */
	@JsonProperty
	public Integer Sort;
	
	/** Statkue Tabelle IT.NRW: gueltigVon */
	@JsonProperty
	public Integer gueltigVon;
	
	/** Statkue Tabelle IT.NRW: gueltigBis */
	@JsonProperty
	public Integer gueltigBis;


	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataStatkueLehrerFachrichtungAnerkennung> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataStatkueLehrerFachrichtungAnerkennung> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_LehrerFachrAnerkennung.csv", DataStatkueLehrerFachrichtungAnerkennung.class); 
		return cache;
	}

}