package de.nrw.schule.svws.csv.statkue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.nrw.schule.svws.csv.converter.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.CsvReader;
import de.nrw.schule.svws.csv.converter.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle Statkue_SVWS_ZulaessigeFaecher.
 */
@JsonPropertyOrder({"Fach","Bezeichnung","FachkuerzelAtomar","Kurzbezeichnung","Aufgabenfeld","Fachgruppe_ID","SchulformenUndGliederungen","SchulformenAusgelaufen","AusgelaufenInSchuljahr","AbJahrgang","IstFremdsprache","IstHKFS","IstAusRegUFach","IstErsatzPflichtFS","IstKonfKoop","NurSII","ExportASD","gueltigVon","gueltigBis"})
public class DataSVWSZulaessigeFaecherMapping {

	/** Fachkürzel aus der Tabelle StatkueZulaessigeFaecher von ITNRW */
	@JsonProperty
	public String Fach;

	/** Bezeichnung aus der Tabelle Faecher von ITNRW */
	@JsonProperty
	public String Bezeichnung;

	/** atomarisiertes Fachkürzel */
	@JsonProperty
	public String FachkuerzelAtomar;

	/** kürzere Bezeichnung falls gewünscht */
	@JsonProperty
	public String Kurzbezeichnung;

	/** das Aufgabenfeld, welchem das Fach zugeordnet ist (1, 2 oder 3)  */
	@JsonProperty
	public Integer Aufgabenfeld;

	/** Die ID der zugeordneten Fachgruppe */
	@JsonProperty
	public Long Fachgruppe_ID;

	/** Gibt die Schulformen an in denen das Fach erlaubt ist (R,H,2020) gibt an dass ein Fach an R nur in H ab 2020 erlaubt ist */
	@JsonProperty
	public String SchulformenUndGliederungen;

	/** Gibt an ab wann ein Fach in einer Schulform ausgelaufen ist (R,H,2020) gibt an in welcher SF und SQL ausgelaufen ist */
	@JsonProperty
	public String SchulformenAusgelaufen;

	/** Gibt an ob ein Fach in Schuljahr komplett ausgelaufen ist */
	@JsonProperty
	public String AusgelaufenInSchuljahr;

	/** Jahrgang ab dem das Fach unterrichtet werden darf */
	@JsonProperty
	public String AbJahrgang;

	/** Boolscher Wert ob es eine Fremdsprache ist */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstFremdsprache;

	/** Boolscher Wert ob es eine HerkunftsFremdsprache ist */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstHKFS;

	/** Boolscher Wert ob es ein reguläres Unterrichtsfach ist */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstAusRegUFach;

	/** Boolscher Wert ob es eine Ersatzpflichfremdsprache ist */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstErsatzPflichtFS;

	/** Boolscher Wert ob es eine Religionslehre im KOOP ist */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstKonfKoop;

	/** Boolscher Wert ob das Fach nur in der SII unterrichtet werden darf */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean NurSII;

	/** Boolscher Wert ob das Fach für den ASDExport vorgesehen ist */
	@JsonProperty
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean ExportASD;

	/** Das Schuljahr, ab dem das Fach eingeführt wurde */
	@JsonProperty
	public Integer gueltigVon;

	/** Das Schuljahr, bis wann das Fach gültig ist */
	@JsonProperty
	public Integer gueltigBis;



	/** Der Cache für die Daten der CSV-Tabelle */
	private static List<DataSVWSZulaessigeFaecherMapping> cache = null;


	/**
	 * Liest alle Datensätze aus der CSV-Tabelle ein.
	 * 	
	 * @return die Datensätze der CSV-Tabelle.
	 */
	public static List<DataSVWSZulaessigeFaecherMapping> get() {
		if (cache == null)
			cache = CsvReader.fromResource("schema/csv/statkue/Statkue_SVWS_ZulaessigeFaecher.csv", DataSVWSZulaessigeFaecherMapping.class); 
		return cache;
	}

}