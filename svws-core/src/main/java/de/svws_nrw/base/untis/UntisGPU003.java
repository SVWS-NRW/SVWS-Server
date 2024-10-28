package de.svws_nrw.base.untis;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import de.svws_nrw.base.CsvReader;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU003.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Klassen.
 */
public class UntisGPU003 {

	/** Das Kürzel der Klasse (z.B. "05A") */
	public String kuerzel;

	/** Die Bezeichnung der Klasse (z.B. "Klasse 05a") */
	public String bezeichnung;

	/** Statistik */
	public String statistik;

	/** Das Kürzel des Raumes (z.B. "") */
	public String raumKuerzel;

	/** Das Kennzeichen */
	public String kennzeichen;

	/** Ignorieren - Dummy - früher für die Farbe benutzt */
	public String dummyFarbe;

	/** Minimum Stunden pro Tag */
	public Integer minStdProTag;

	/** Maximum Stunden pro Tag */
	public Integer maxStdProTag;

	/** Minimum Mittagspause */
	public Integer minMittagspause;

	/** Maximum Mittagspause */
	public Integer maxMittagspause;

	/** Hauptfolge */
	public Integer hauptfolge;

	/** Hauptfolge hint. */
	public Integer hauptfolgeHint;

	/** Klassengruppe */
	public String klassenGruppe;

	/** Schulstufe */
	public Integer schulstufe;

	/** Abteilung */
	public String abteilung;

	/** Faktor */
	public String faktor;

	/** Stundenten männl. */
	public Integer studentenM;

	/** Stundenten weibl. */
	public Integer studentenW;

	/** Schulform */
	public String schulform;

	/** Unterrichtsbeginn (Datum) */
	public String unterrichtsbeginnDatum;

	/** Unterrichtsende (Datum) */
	public String unterrichtsendeDatum;

	/** Sondertext */
	public String sondertext;

	/** Beschreibung */
	public String beschreibung;

	/** Farbe Vordergrund */
	public String farbeVordergrund;

	/** Farbe Hintergrund */
	public String farbeHintergrund;

	/** Statistik 2 */
	public String statistik2;

	/** Das Kürzel der Klasse im Vorjahr (z.B. "05A") */
	public String kuerzelVorjahr;

	/** Faktor (nur Export) */
	public Double faktorExport;

	/** Alias */
	public String alias;

	/** Das Kürzel des Klassenlehrers (z.B. "BACH") */
	public String kuerzelKlassenlehrer;

	/** Das Kürzel der Hauptklasse - relevant bei Verwendung von Teilklassen */
	public String kuerzelHauptklasse;

	/** Stundenten interges. */
	public Integer studentenI;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;


	/** Das CSV-Schema */
	private static final CsvSchema csvSchema = CsvSchema.builder()
			.addColumn("kuerzel")
			.addColumn("bezeichnung")
			.addColumn("statistik")
			.addColumn("raumKuerzel")
			.addColumn("kennzeichen")
			.addColumn("dummyFarbe")
			.addNumberColumn("minStdProTag")
			.addNumberColumn("maxStdProTag")
			.addNumberColumn("minMittagspause")
			.addNumberColumn("maxMittagspause")
			.addNumberColumn("hauptfolge")
			.addNumberColumn("hauptfolgeHint")
			.addColumn("klassenGruppe")
			.addNumberColumn("schulstufe")
			.addColumn("abteilung")
			.addColumn("faktor")
			.addNumberColumn("studentenM")
			.addNumberColumn("studentenW")
			.addColumn("schulform")
			.addColumn("unterrichtsbeginnDatum")
			.addColumn("unterrichtsendeDatum")
			.addColumn("sondertext")
			.addColumn("beschreibung")
			.addColumn("farbeVordergrund")
			.addColumn("farbeHintergrund")
			.addColumn("statistik2")
			.addColumn("kuerzelVorjahr")
			.addNumberColumn("faktorExport")
			.addColumn("alias")
			.addColumn("kuerzelKlassenlehrer")
			.addColumn("kuerzelHauptklasse")
			.addNumberColumn("studentenI")
			.addColumn("dummy")
			.build()
			.withColumnSeparator(';')
			.withQuoteChar('\"')
			.withNullValue("")
			.withoutHeader();

	/** Die Instanz des Object-Writers für die CSV-Daten */
	private static final ObjectWriter writer = new CsvMapper().writerFor(UntisGPU003.class).with(csvSchema).with(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UntisGPU003() {
		// leer
	}

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU003-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als Byte-Array
	 *
	 * @return eine Liste mit den GPU003-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<UntisGPU003> readCSV(final byte[] csvData) throws IOException {
		return CsvReader.fromUntis(UntisGPU003.class, csvSchema, csvData);
	}


	/**
	 * Erstellt aus der übergebenen Liste der DTOs die CSV-Daten als String
	 *
	 * @param dtos   die Liste der DTOs
	 *
	 * @return die CSV-Daten als UTF-8 String
	 *
	 * @throws IOException falls die CSV-Daten nicht erstellt werden können
	 */
	@SuppressWarnings("resource")
	public static String writeCSV(final @NotNull List<UntisGPU003> dtos) throws IOException {
		final StringWriter sw = new StringWriter();
		writer.writeValues(sw).writeAll(dtos).close();
		return sw.toString();
	}

}
