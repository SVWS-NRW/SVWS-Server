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
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU004.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Lehrkräfte.
 */
public class UntisGPU004 {

	/** (1) Name: Das Kürzel der Lehrkraft (z.B. "MEI") */
	public String kuerzel;

	/** (2) Langname: Der Nachname der Lehrkraft (z.B. Meier) */
	public String nachname;

	/** (3) Statistik 1 (Kürzel Rechtsverhältnis, bei Teilzeit: "RV,T") */
	public String statistik1;

	/** (4) Personal-Nummer */
	public String persNr;

	/** (5) Stammraum */
	public String raum;

	/** (6) Kennzeichen */
	public String kennzeichen;

	/** (7) Frei (ehemals Farbe) */
	public String dummy1;

	/** (8) Stunden pro Tag - minimal */
	public Integer minStdProTag;

	/** (9) Stunden pro Tag - maximal */
	public Integer maxStdProTag;

	/** (10) Hohlstunden - minimal */
	public Integer minHohlstd;

	/** (11) Hohlstunden - maximal */
	public Integer maxHohlstd;

	/** (12) Mittagspausen - minimal */
	public Integer minMittagspausen;

	/** (13) Mittagspausen - maximal */
	public Integer maxMittagspausen;

	/** (14) maximale Länge Stundenfolge */
	public Integer maxStundenfolge;

	/** (15) Wochen-Soll */
	public Double wochenSoll;

	/** (16) Wochen-Wert */
	public Double wochenWert;

	/** (17) Abteilung 1 */
	public String abteilung1;

	/** (18) Wert-Faktor */
	public String wertFaktor;

	/** (19) Abteilung 2 */
	public String abteilung2;

	/** (20) Abteilung 3 */
	public String abteilung3;

	/** (21) Status */
	public String status;

	/** (22) Jahres-Soll */
	public Double jahresSoll;

	/** (23) Text */
	public String text;

	/** (24) Beschreibung */
	public String beschreibung;

	/** (25) Farbe Vordergrund */
	public String farbeVordergrund;

	/** (26) Farbe Hintergrund */
	public String farbeHintergrund;

	/** (27) Statistik 2 */
	public String statistik2;

	/** (28) berechneter Faktor */
	public Double faktorBerechnet;

	/** (29) Vorname */
	public String vorname;

	/** (30) Titel */
	public String titel;

	/** (31) Das Geschlecht der Lehrkraft (1 = weiblich, 2 = männlich) */
	public String geschlecht;

	/** (32) Stammschule */
	public String stammschule;

	/** (33) E-Mail Adresse */
	public String eMail;

	/** (34) Sperrvermerk (Modul Vertretungsplanung) */
	public String sperrvermerk;

	/** (35) Wochen-Soll maximal */
	public String maxWochenSoll;

	/** (36) Alias */
	public String alias;

	/** (37) Personalnummer 2 */
	public String persNr2;

	/** (38) Stundensatz (Vergütungsschlüssel) */
	public String stundensatz;

	/** (39) Telefonnummer */
	public String telefon;

	/** (40) Mobiltelefonnummer */
	public String telefonMobil;

	/** (41) Geburtsdatum */
	public String geburtsdatum;

	/** (42) Name als externes Element */
	public String extName;

	/** (43) Text2 */
	public String text2;

	/** (44) Eintrittsdatum */
	public String datumZugang;

	/** (45) Austrittsdatum */
	public String datumAbgang;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;


	/** Das CSV-Schema */
	private static final CsvSchema csvSchema = CsvSchema.builder()
			.addColumn("kuerzel")
			.addColumn("nachname")
			.addColumn("statistik1")
			.addColumn("persNr")
			.addColumn("raum")
			.addColumn("kennzeichen")
			.addColumn("dummy1")
			.addNumberColumn("minStdProTag")
			.addNumberColumn("maxStdProTag")
			.addNumberColumn("minHohlstd")
			.addNumberColumn("maxHohlstd")
			.addNumberColumn("minMittagspausen")
			.addNumberColumn("maxMittagspausen")
			.addNumberColumn("maxStundenfolge")
			.addNumberColumn("wochenSoll")
			.addNumberColumn("wochenWert")
			.addColumn("abteilung1")
			.addColumn("wertFaktor")
			.addColumn("abteilung2")
			.addColumn("abteilung3")
			.addColumn("status")
			.addNumberColumn("jahresSoll")
			.addColumn("text")
			.addColumn("beschreibung")
			.addColumn("farbeVordergrund")
			.addColumn("farbeHintergrund")
			.addColumn("statistik2")
			.addNumberColumn("faktorBerechnet")
			.addColumn("vorname")
			.addColumn("titel")
			.addColumn("geschlecht")
			.addColumn("stammschule")
			.addColumn("eMail")
			.addColumn("sperrvermerk")
			.addColumn("maxWochenSoll")
			.addColumn("alias")
			.addColumn("persNr2")
			.addColumn("stundensatz")
			.addColumn("telefon")
			.addColumn("telefonMobil")
			.addColumn("geburtsdatum")
			.addColumn("extName")
			.addColumn("text2")
			.addColumn("datumZugang")
			.addColumn("datumAbgang")
			.addColumn("dummy")
			.build()
			.withColumnSeparator(';')
			.withQuoteChar('\"')
			.withNullValue("")
			.withoutHeader();

	/** Die Instanz des Object-Writers für die CSV-Daten */
	private static final ObjectWriter writer = new CsvMapper().writerFor(UntisGPU004.class).with(csvSchema).with(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UntisGPU004() {
		// leer
	}

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU004-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als Byte-Array
	 *
	 * @return eine Liste mit den GPU004-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<UntisGPU004> readCSV(final byte[] csvData) throws IOException {
		return CsvReader.fromUntis(UntisGPU004.class, csvSchema, csvData);
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
	public static String writeCSV(final @NotNull List<UntisGPU004> dtos) throws IOException {
		final StringWriter sw = new StringWriter();
		writer.writeValues(sw).writeAll(dtos).close();
		return sw.toString();
	}

}
