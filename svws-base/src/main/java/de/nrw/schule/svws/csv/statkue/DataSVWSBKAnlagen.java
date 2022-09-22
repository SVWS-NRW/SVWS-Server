package de.nrw.schule.svws.csv.statkue;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.csv.CsvReader;


/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_SVWS_BKAnlagen.
 */
@JsonPropertyOrder({"BKAnlage","Beschreibung","gueltigVon","gueltigBis"})
public class DataSVWSBKAnlagen {

	/** Einstelliges Kürzel der BK-Anlage (A,B,C,D,E) */
	@JsonProperty
	public String BKAnlage;

	/** Textuelle Beschreibung der BK-Anlage */
	@JsonProperty
	public String Beschreibung;
	
	/** Statkue Tabelle BK-Anlage gueltigVon */
	@JsonProperty
	public Integer gueltigVon;
	
	/** Statkue Tabelle BK-Anlage gueltigBis */
	@JsonProperty
	public Integer gueltigBis;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataSVWSBKAnlagen> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataSVWSBKAnlagen> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_SVWS_BKAnlagen.csv", DataSVWSBKAnlagen.class); 
		return cache;
	}

}