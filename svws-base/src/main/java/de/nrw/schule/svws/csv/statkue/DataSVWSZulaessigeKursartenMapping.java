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
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_SVWS_ZulaessigeKursarten.
 */
@JsonPropertyOrder({"ID","Kuerzel","ASDNummer","Bezeichnung","SchulformenUndGliederungen","KuerzelAllg","BezeichnungAllg","SchulformenAusgelaufen","erlaubtGOSt","AusgelaufenInSchuljahr","Bemerkungen","gueltigVon","gueltigBis"})
public class DataSVWSZulaessigeKursartenMapping {

	/** Eine eindeutige ID die auch als Bezeichner in der Programmiersprache genutzt wird */
	@JsonProperty
	public String ID;

	/** das in der Statistik verwendete Kürzel der Kursart ITNRW */
	@JsonProperty
	public String Kuerzel;

	/** die Statistiknummer in der ASD von ITNRW */
	@JsonProperty
	public String ASDNummer;

	/** die Bezeichnung der Kursart in vereinfachter Form für die Anzeige im Client */
	@JsonProperty
	public String Bezeichnung;

	/** Gibt die Schulformen an in denen die Kursart erlaubt ist (R,H,2020) gibt an dass die Kursart an R nur in H ab 2020 erlaubt ist */
	@JsonProperty
	public String SchulformenUndGliederungen;

	/** die allgemeine Kursart die nicht schülerspezifisch ist sonder die gesamte Gruppe betrifft */
	@JsonProperty
	public String KuerzelAllg;

	/** die allgemeine Bezeichnung zur allgemeinen Kursart */
	@JsonProperty
	public String BezeichnungAllg;

	/** Gibt an ab wann die Kursart in einer Schulform ausgelaufen ist (R,H,2020) gibt an in welcher SF und SQL ausgelaufen ist */
	@JsonProperty
	public String SchulformenAusgelaufen;

	/** gibt an ob eine Kursart für die Abiturberechnung belegt werden darf */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean erlaubtGOSt;

	/** Gibt an ob die Kursart in Schuljahr komplett ausgelaufen ist */
	@JsonProperty
	public String AusgelaufenInSchuljahr;

	/** Bemerkungen zu Paragraphen in der BASS */
	@JsonProperty
	public String Bemerkungen;
	
	/** Statkue Tabelle IT.NRW: gueltigVon */
	@JsonProperty
	public Integer gueltigVon;
	
	/** Statkue Tabelle IT.NRW: gueltigBis */
	@JsonProperty
	public Integer gueltigBis;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataSVWSZulaessigeKursartenMapping> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataSVWSZulaessigeKursartenMapping> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_SVWS_ZulaessigeKursarten.csv", DataSVWSZulaessigeKursartenMapping.class); 
		return cache;
	}

}