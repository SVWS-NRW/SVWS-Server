package de.svws_nrw.base.untis;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU002.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Unterrichtsverteilung.
 */
public class UntisGPU002 {

	/** Eine numerische ID, welche den Unterricht eindeutig identifiziert (z.B. 42) */
	public long idUnterricht;

	/** Die Wochenstunden des Unterrichtes */
	public long wochenstunden;

	/** Die Wochenstunden für die Klasse */
	public long wochenstundenKlasse;

	/** Die Wochenstunden des Lehrers */
	public long wochenstundenLehrer;

	/** Das Kürzel der Klasse (z.B. "05A") */
	public String klasseKuerzel;

	/** Das Kürzel des Lehrers (z.B. "BACH") */
	public String lehrerKuerzel;

	/** Das Kürzel des Fachen (z.B. "D") */
	public String fachKuerzel;

	/** Das Kürzel des Raumes (z.B. "") */
	public String raumKuerzel;

	/** Statistik 1 Unt */
	public Integer statistik1Unt;

	/** Studentenzahl */
	public Integer studentenZahl;

	/** Wochenwert */
	public Double wochenwert;

	/** Die Gruppe, welche den Wochentyp bei AB-, ABC-, ABCD-Wochentypmodellen angibt */
	public String wochenTyp;

	/** Zeilentext 1 */
	public String zeilenText1;

	/** Zeilenwert in Tausendstel */
	public String zeilenWert;

	/** Datum von */
	public String datumVon;

	/** Datum bis */
	public String datumBis;

	/** Jahreswert */
	public Double jahreswert;

	/** Text */
	public String text;

	/** Teilungsnummer */
	public String teilungsnummer;

	/** Kürzel des Stammraums */
	public String stammraumKuerzel;

	/** Beschreibung */
	public String beschreibung;

	/** Farbe Vordergrund */
	public String farbeVordergrund;

	/** Farbe Hintergrund */
	public String farbeHintergrund;

	/** Kennzeichen */
	public String kennzeichen;

	/** Fachfolge Klassen */
	public String fachfolgeKlassen;

	/** Fachfolge Lehrer */
	public String fachfolgeLehrer;

	/** Klassen-Kollisions-Kennz. */
	public String klassenKollKennz;

	/** Doppelstd. min. */
	public Integer doppelStdMin;

	/** Doppelstd. max. */
	public Integer doppelStdMax;

	/** Blockgröße */
	public Integer blockgroesse;

	/** Stunden im Raum */
	public Double stundenImRaum;

	/** Priorität */
	public String prioritaet;

	/** Statistik 1 Lehrer */
	public String statistik1Lehrer;

	/** Studenten männlich */
	public Integer studentenMaennlich;

	/** Studenten weiblich */
	public Integer studentenWeiblich;

	/** Wert bzw. Faktor */
	public String wert;

	/** 2. Block */
	public String block2;

	/** 3. Block */
	public String block3;

	/** Zeilentext 2 */
	public String zeilenText2;

	/** Eigenwert */
	public String eigenwert;

	/** Eigenwert in hundertausendstel */
	public String eigenwertHunderttausendstel;

	/** Schülergruppe */
	public String schuelergruppe;

	/** Wochenstunden in Jahres-Perioden-Planung */
	public String wochenstundenJahresperioden;

	/** Jahresstunden */
	public String jahresstunden;

	/** Zeilen-Unterrichtsgruppe */
	public String zeilenUnterrichtsgruppe;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;


	/** Die Instanz des Object-Readers für die CSV-Daten */
	private static final ObjectReader reader = new CsvMapper().readerFor(UntisGPU002.class).with(CsvSchema.builder()
		.addNumberColumn("idUnterricht")
		.addNumberColumn("wochenstunden")
		.addNumberColumn("wochenstundenKlasse")
		.addNumberColumn("wochenstundenLehrer")
		.addColumn("klasseKuerzel")
		.addColumn("lehrerKuerzel")
		.addColumn("fachKuerzel")
		.addColumn("raumKuerzel")
		.addNumberColumn("statistik1Unt")
		.addNumberColumn("studentenZahl")
		.addNumberColumn("wochenwert")
		.addColumn("wochenTyp")
		.addColumn("zeilenText1")
		.addColumn("zeilenWert")
		.addColumn("datumVon")
		.addColumn("datumBis")
		.addNumberColumn("jahreswert")
		.addColumn("text")
		.addColumn("teilungsnummer")
		.addColumn("stammraumKuerzel")
		.addColumn("beschreibung")
		.addColumn("farbeVordergrund")
		.addColumn("farbeHintergrund")
		.addColumn("kennzeichen")
		.addColumn("fachfolgeKlassen")
		.addColumn("fachfolgeLehrer")
		.addColumn("klassenKollKennz")
		.addNumberColumn("doppelStdMin")
		.addNumberColumn("doppelStdMax")
		.addNumberColumn("blockgroesse")
		.addNumberColumn("stundenImRaum")
		.addColumn("prioritaet")
		.addColumn("statistik1Lehrer")
		.addNumberColumn("studentenMaennlich")
		.addNumberColumn("studentenWeiblich")
		.addColumn("wert")
		.addColumn("block2")
		.addColumn("block3")
		.addColumn("zeilenText2")
		.addColumn("eigenwert")
		.addColumn("eigenwertHunderttausendstel")
		.addColumn("schuelergruppe")
		.addColumn("wochenstundenJahresperioden")
		.addColumn("jahresstunden")
		.addColumn("zeilenUnterrichtsgruppe")
		.addColumn("dummy")
		.build()
		.withColumnSeparator(';')
		.withQuoteChar('\"')
		.withNullValue("")
		.withoutHeader()
	);

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU002-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als String
	 *
	 * @return eine Liste mit den GPU002-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<@NotNull UntisGPU002> readCSV(final String csvData) throws IOException {
		try (MappingIterator<UntisGPU002> it = reader.readValues(csvData)) {
			return it.readAll();
		}
	}

}
