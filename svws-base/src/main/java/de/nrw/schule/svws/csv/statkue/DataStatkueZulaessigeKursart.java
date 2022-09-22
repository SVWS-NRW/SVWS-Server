package de.nrw.schule.svws.csv.statkue;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.csv.CsvReader;


/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_ZulKuArt.
 */
@JsonPropertyOrder({"SF","FSP","BG","Kursart","Kursart2","Bezeichnung","SGLBereich","Flag","geaendert","gueltigVon","gueltigBis"})
public class DataStatkueZulaessigeKursart {

	/** Katalog von ITNRW Schulform der zulässigen Kursarten */
	@JsonProperty
	public String SF;

	/** Katalog von ITNRW Förderschwerpunkt der zulässigen Kursarten */
	@JsonProperty
	public String FSP;

	/** Katalog von ITNRW BG der zulässigen Kursarten */
	@JsonProperty
	public String BG;

	/** Katalog von ITNRW Kursartschlüssel der zulässigen Kursarten */
	@JsonProperty
	public String Kursart;

	/** Katalog von ITNRW Kursartkurztext der zulässigen Kursarten */
	@JsonProperty
	public String Kursart2;

	/** Katalog von ITNRW Langtext der der zulässigen Kursarten */
	@JsonProperty
	public String Bezeichnung;

	/** Katalog von ITNRW Jahrgangsbereich der zulässigen Kursarten */
	@JsonProperty
	public Integer SGLBereich;

	/** Katalog von ITNRW: 1 = zulässig für Hauptzeilen in der UVD 2 = zulässig für Folgezeilen in der UVD 3 = gelöscht kann im Client auf 1 gefiltert sein */
	@JsonProperty
	public String Flag;

	/** Katalog von ITNRW Änderungsdatum der zulässigen Kursarten */
	@JsonProperty
	public String geaendert;
	
	/** Katalog von IT.NRW: gueltigVon */
	@JsonProperty
	public Integer gueltigVon;
	
	/** Katalog von  IT.NRW: gueltigBis */
	@JsonProperty
	public Integer gueltigBis;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataStatkueZulaessigeKursart> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataStatkueZulaessigeKursart> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_ZulKuArt.csv", DataStatkueZulaessigeKursart.class); 
		return cache;
	}

}