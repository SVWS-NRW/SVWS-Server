package de.nrw.schule.svws.csv.statkue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.csv.CsvReader;


/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_ZulFaecher.
 */
@JsonPropertyOrder({"Schulform","FSP","BG","Fach","Bezeichnung","KZ_Bereich","Flag","Sortierung","Sortierung2","geaendert","gueltigVon","gueltigBis"})
public class DataStatkueZulaessigesFach {

	/** Katalog von ITNRW  Schulform in der das zulässige FAch vorkommen darf */
	@JsonProperty
	public String Schulform;

	/** Katalog von ITNRW  Förderschwerpunkt  in dem das zulässige FAch vorkommen darf */
	@JsonProperty
	public String FSP;

	/** Katalog von ITNRW  ??? */
	@JsonProperty
	public String BG;

	/** Katalog von ITNRW  Fachkürzel */
	@JsonProperty
	public String Fach;

	/** Katalog von ITNRW  Klartext des Faches */
	@JsonProperty
	public String Bezeichnung;

	/** Katalog von ITNRW  wird nur noch von IT.NRW aus technischen Gründen benötigt für Cllient irrelevant */
	@JsonProperty
	public Integer KZ_Bereich;

	/** Katalog von ITNRW  1 = zulässig für die Hauptzeilen in der UVD 2 = zulässig für die Folgezeilen in der UVD (Anmerkung: nur Fremdsprachen) 3 = nur für SchILD */
	@JsonProperty
	public String Flag;

	/** Katalog von ITNRW  Sortierung des Faches in ASDPC32 primär */
	@JsonProperty
	public Integer Sortierung;

	/** Katalog von ITNRW  Sortierung2 des Faches in ASDPC32 sekundär */
	@JsonProperty
	public Integer Sortierung2;

	/** Katalog von ITNRW  Datum der letzten Änderung */
	@JsonProperty
	public String geaendert;
	
	/** Katalog von IT.NRW: gueltigVon */
	@JsonProperty
	public Integer gueltigVon;
	
	/** Katalog von IT.NRW: gueltigBis */
	@JsonProperty
	public Integer gueltigBis;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataStatkueZulaessigesFach> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataStatkueZulaessigesFach> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_ZulFaecher.csv", DataStatkueZulaessigesFach.class); 
		return cache;
	}

}