package de.nrw.schule.svws.csv.statkue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.csv.CsvReader;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_Schulformen.
 */
@JsonPropertyOrder({"Schulform","SF","Bezeichnung","Flag","geaendert","Sortierung","gueltigVon","gueltigBis"})
public class DataStatkueSchulform {

	/** Statkue Tabelle IT.NRW: Schulformnummer */
	@JsonProperty
	public String Schulform;

	/** Statkue Tabelle IT.NRW: Schulformkürzel */
	@JsonProperty
	public String SF;

	/** Statkue Tabelle IT.NRW: Klartext der Schulform */
	@JsonProperty
	public String Bezeichnung;

	/** Statkue Tabelle IT.NRW: ??? */
	@JsonProperty
	public String Flag;

	/** Statkue Tabelle IT.NRW:  Datum der letzten Änderung */
	@JsonProperty
	public String geaendert;

	/** Statkue Tabelle IT.NRW: Sortierung der Schulformen */
	@JsonProperty
	public Integer Sortierung;
	
	/** Statkue Tabelle IT.NRW: gueltigVon */
	@JsonProperty
	public Integer gueltigVon;
	
	/** Statkue Tabelle IT.NRW: gueltigBis */
	@JsonProperty
	public Integer gueltigBis;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataStatkueSchulform> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataStatkueSchulform> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_Schulformen.csv", DataStatkueSchulform.class); 
		return cache;
	}

}