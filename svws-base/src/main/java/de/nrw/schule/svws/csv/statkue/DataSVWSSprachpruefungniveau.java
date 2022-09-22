package de.nrw.schule.svws.csv.statkue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.nrw.schule.svws.csv.CsvReader;

import java.util.List;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_SVWS_SprachpruefungNiveau.
 */
@JsonPropertyOrder({"ID","Bezeichnung","Beschreibung","Sortierung","GueltigVon","GueltigBis"})
public class DataSVWSSprachpruefungniveau {

	/** ID des Sprachpruefungsniveaus*/
	@JsonProperty
	public Integer ID;

	/** Bezeichnung des Niveaus der Sprachprüfung, angelehnt an einen Schulabschluss */
	@JsonProperty
	public String Bezeichnung;

	/** Beschreibung des Niveaus der Sprachprüfung */
	@JsonProperty
	public String Beschreibung;

	/** Sortierung der Anspruchsniveaus der Prüfung  */
	@JsonProperty
	public Integer Sortierung;

	/** Schuljahr, ab dem das Niveau gültig ist */
	@JsonProperty
	public Integer gueltigVon;

	/** Schuljahr, bis zu dem das  Niveau gültig ist */
	@JsonProperty
	public Integer gueltigBis;


	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataSVWSSprachpruefungniveau> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataSVWSSprachpruefungniveau> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_SVWS_SprachpruefungNiveaus.csv", DataSVWSSprachpruefungniveau.class);
		return cache;
	}

}