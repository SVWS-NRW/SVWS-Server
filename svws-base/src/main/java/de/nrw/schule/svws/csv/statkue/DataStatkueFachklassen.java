package de.nrw.schule.svws.csv.statkue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.nrw.schule.svws.csv.CsvReader;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_Foerderschwerpunkt.
 */
public class DataStatkueFachklassen {

	/** Der Index für einen Bildungsgang des Berufskollegs in die Fachklassen-Tabelle */
	@JsonProperty
	public String BKIndex;
	
	/** 1 - aktiv, 2 - einmalig beim auslaufen der Fachklasse, 3 - ausgelaufen, 4 - im Jahr neu eingeführt */
	@JsonProperty
	public String Flag;

	/** Fachklassenschlüssel */
	@JsonProperty
	public String FKS;

	/** Fachklassenschlüssel Teil 2 */
	@JsonProperty
	public String AP;
	
	/** Berufsfeldgruppe */
	@JsonProperty
	public String BGrp;
	
	/** Berufsfeld */
	@JsonProperty
	public String BFeld;
	
	/** Berufsebene 1 */
	@JsonProperty
	public String Ebene1;
	
	/** Berufsebene 2 */
	@JsonProperty
	public String Ebene2;
	
	/** Berufsebene 3 */
	@JsonProperty
	public String Ebene3;

	/** BAKL - Feld für die Liefersystematik des statistischen Bundesamtes */
	@JsonProperty
	public String BAKL;

	/** BAGR - Feld für die Liefersystematik des statistischen Bundesamtes */
	@JsonProperty
	public String BAGR;

	/** BAKLALT - Feld für die Liefersystematik des statistischen Bundesamtes */
	@JsonProperty
	public String BAKLALT;

	/** BAGRALT - Feld für die Liefersystematik des statistischen Bundesamtes */
	@JsonProperty
	public String BAGRALT;
	
	/** Sortierung */
	@JsonProperty
	public Integer Sortierung;

	/** Status */
	@JsonProperty
	public String Status;
	
	/** ??? */
	@JsonProperty
	public String Flag_APOBK;
	
	/** Beschreibung der Fachklasse (neutral) */
	@JsonProperty
	public String Beschreibung_MW;

	/** Beschreibung der Fachklasse (männlich) */
	@JsonProperty
	public String Beschreibung;

	/** Beschreibung der Fachklasse (weiblich) */
	@JsonProperty
	public String Beschreibung_W;
	
	/** letztes Datum der Änderung des Förderschwerpunkts */
	@JsonProperty
	public String geaendert;

	/** gueltigVon */
	@JsonProperty
	public Integer gueltigVon;
	
	/** gueltigBis */
	@JsonProperty
	public Integer gueltigBis;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataStatkueFachklassen> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataStatkueFachklassen> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_Fachklasse.csv", DataStatkueFachklassen.class); 
		return cache;
	}

}