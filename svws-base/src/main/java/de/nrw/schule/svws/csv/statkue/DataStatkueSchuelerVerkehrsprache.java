package de.nrw.schule.svws.csv.statkue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.csv.CsvReader;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_SchuelerVerkehrssprache
 */
@JsonPropertyOrder({"ID","Kurztext","Langtext","Gesprochen_in","Beginn","Ende","gueltigVon","gueltigBis"})
public class DataStatkueSchuelerVerkehrsprache {

	/** Statkue Tabelle IT.NRW: ID */
	@JsonProperty
	public Integer ID;

	/** Statkue Tabelle IT.NRW: Kürzel der Verkehrssprache */
	@JsonProperty
	public String Kurztext;

	/** Statkue Tabelle IT.NRW: Bezeichnung der Verkehrssprache */
	@JsonProperty
	public String Langtext;

	/** Statkue Tabelle IT.NRW: Gesprochen in */
	@JsonProperty
	public String Gesprochen_in;

	/** Statkue Tabelle IT.NRW: ??? */
	@JsonProperty
	public String Beginn;

	/** Statkue Tabelle IT.NRW:  ??? */
	@JsonProperty
	public String Ende;

	/** Statkue Tabelle IT.NRW: gueltigVon */
	@JsonProperty
	public Integer gueltigVon;
	
	/** Statkue Tabelle IT.NRW: gueltigBis */
	@JsonProperty
	public Integer gueltigBis;


	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataStatkueSchuelerVerkehrsprache> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataStatkueSchuelerVerkehrsprache> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_SchuelerVerkehrssprache.csv", DataStatkueSchuelerVerkehrsprache.class); 
		return cache;
	}

}