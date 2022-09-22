package de.nrw.schule.svws.csv.statkue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.nrw.schule.svws.csv.CsvReader;
import de.nrw.schule.svws.csv.converter.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_SVWS_Schulgliederungen.
 */
@JsonPropertyOrder({"SGL","istBK","Schulformen","istAuslaufend","istAusgelaufen","Beschreibung","BKAnlage","BKTyp","BKIndex","istVZ","BKAbschlussBeruf","BKAbschlussAllg"})
public class DataSVWSSchulgliederungen {

	/** Das dreistellige Kürzel der Schulgliederung bzw. des Bildungsganges */
	@JsonProperty
	public String SGL;

	/** Boolscher Wert ob es sich um eine Schulgliederung einer BK-Anlage handelt */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean istBK;

	/** eine Komma-separierte Liste der Schulform-Kürzel, bei denen der Bildungsgang vorhanden ist */
	@JsonProperty
	public String Schulformen;

	/** Boolscher Wert ob es sich um eine auslaufende Schulgliederung handelt */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean istAuslaufend;

	/** Boolscher Wert ob die Schulgliederung bereits ausgelaufen ist */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean istAusgelaufen;

	/** die textuelle Beschreibung der Schulgliederung */
	@JsonProperty
	public String Beschreibung;

	/** die Anlage bei einem Bildungsgang des Berufskollegs */
	@JsonProperty
	public String BKAnlage;

	/** der Typ der Anlage bei einem Bildungsgang des Berufskollegs */
	@JsonProperty
	public String BKTyp;

	/** der Index in die Fachklassen-Tabelle einem Bildungsgang des Berufskollegs */
	@JsonProperty
	public String BKIndex;

	/** Boolscher Wert ob ein Bildungsgang des Berufskollegs Vollzeit erfordert   */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean istVZ;

	/** der berufsbildende Abschluss bei einem Bildungsgang des Berufskollegs */
	@JsonProperty
	public String BKAbschlussBeruf;

	/** del allgemeinbildende Abschluss bei einem Bildungsgang des Berufskollegs */
	@JsonProperty
	public String BKAbschlussAllg;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataSVWSSchulgliederungen> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataSVWSSchulgliederungen> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_SVWS_Schulgliederungen.csv", DataSVWSSchulgliederungen.class); 
		return cache;
	}

}