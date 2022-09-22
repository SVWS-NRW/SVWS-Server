package de.nrw.schule.svws.csv.statkue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.csv.CsvReader;


/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_SVWS_Fachgruppen.
 */
@JsonPropertyOrder({"ID","Fachbereich","SchildFgID","FG_Bezeichnung","FG_Kuerzel","Schulformen","FarbeR","FarbeG","FarbeB","gueltigVon","gueltigBis"})
public class DataSVWSFachgruppen {

	/** ID in der von SVWS erstellten Tabelle mit Fachgruppen die zu SchildNRW und Lupo passen Mapping */
	@JsonProperty
	public Long ID;

	/** Fachbereich (Nr) der Fachgruppe aus Lupo */
	@JsonProperty
	public Integer Fachbereich;

	/** Fachgruppen ID aus SchildNRW */
	@JsonProperty
	public Long SchildFgID;

	/** Bezeichnung der Fachgruppe */
	@JsonProperty
	public String FG_Bezeichnung;

	/** Kürzel der Fachgruppe */
	@JsonProperty
	public String FG_Kuerzel;

	/** Gibt an in welchen Schulformen die Fachgruppe zulässig ist */
	@JsonProperty
	public String Schulformen;

	/** RotWert der Fachgruppenfarbe */
	@JsonProperty
	public Integer FarbeR;

	/** GrünWert der Fachgruppenfarbe */
	@JsonProperty
	public Integer FarbeG;

	/** BlauWert der Fachgruppenfarbe */
	@JsonProperty
	public Integer FarbeB;

	/** Das Schuljahr, ab dem die Fachgruppe eingeführt wurde */
	@JsonProperty
	public Integer gueltigVon;

	/** Das Schuljahr, bis wann die Fachgruppe gültig ist */
	@JsonProperty
	public Integer gueltigBis;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataSVWSFachgruppen> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataSVWSFachgruppen> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_SVWS_Fachgruppen.csv", DataSVWSFachgruppen.class); 
		return cache;
	}

}