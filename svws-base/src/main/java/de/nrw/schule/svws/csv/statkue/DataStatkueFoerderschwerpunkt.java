package de.nrw.schule.svws.csv.statkue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.csv.CsvReader;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_Foerderschwerpunkt.
 */
@JsonPropertyOrder({"Beschreibung","Flag","FSP","geaendert","SF","gueltigVon","gueltigBis"})
public class DataStatkueFoerderschwerpunkt {

	/** Statkue Tabelle IT.NRW: Beschreibung des Förderschwerpunkts */
	@JsonProperty
	public String Beschreibung;

	/** Statkue Tabelle IT.NRW: ??? */
	@JsonProperty
	public String Flag;

	/** Statkue Tabelle IT.NRW: StatistikKürzel des Förderschwerpunkts */
	@JsonProperty
	public String FSP;

	/** Statkue Tabelle IT.NRW: letztes Datum der Änderung des Förderschwerpunkts */
	@JsonProperty
	public String geaendert;

	/** Statkue Tabelle IT.NRW: zulässige Schulform des Förderschwerpunkts */
	@JsonProperty
	public String SF;
	
	/** Statkue Tabelle IT.NRW: gueltigVon */
	@JsonProperty
	public Integer gueltigVon;
	
	/** Statkue Tabelle IT.NRW: gueltigBis */
	@JsonProperty
	public Integer gueltigBis;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataStatkueFoerderschwerpunkt> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataStatkueFoerderschwerpunkt> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_Foerderschwerpunkt.csv", DataStatkueFoerderschwerpunkt.class); 
		return cache;
	}

}