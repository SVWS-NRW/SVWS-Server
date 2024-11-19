package de.svws_nrw.base.untis;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import de.svws_nrw.base.CsvReader;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU006.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Fächer bzw. Kurse.
 */
public final class UntisGPU006 {

	/** (1) Name - Das Kürzel des Faches bzw. des Kurses */
	public String kuerzel;

	/** (2) Langname - Die Bezeichnung des Faches bzw. des Kurses */
	public String bezeichnung;

	/** (3) Statistik 1 */
	public String statistik1;

	/** (4) Raum */
	public String raum;

	/** (5) Kennzeichen - "H" für Hauptfach, "R" für Randstundenfach, "F" für Freifach, "2" mehrmals an einem Tag, "G" nicht in Randstunden, "D", "E", "P", "S" (siehe Untis-Dokumentation */
	public String kennzeichen;

	/** (6) Frei (Farbe) */
	public String farbe;

	/** (7) Wochenstd. min. */
	public Double minWochenstunden;

	/** (8) Wochenstd. max. */
	public Double maxWochenstunden;

	/** (9) Nachmittag min. */
	public Double minNachmittag;

	/** (10) Nachm. max. */
	public Double maxNachmittag;

	/** (11) Fachfolge Klasse */
	public String fachfolgeKlasse;

	/** (12) Fachfolge Lehrkraft */
	public String fachfolgeLehrkraft;

	/** (13) Fach-Gruppe */
	public String fachgruppe;

	/** (14) Faktor */
	public String faktorWert;

	/** (15) Faktor */
	public Double faktor;

	/** (16) Text - kann z.B. so gestaltet sein, dass er in Untis zum Sortieren genutzt wird */
	public String text;

	/** (17) Beschreibung */
	public String beschreibung;

	/** (18) Vordergrund-Farbe */
	public String farbeVordergrund;

	/** (19) Hintergrund-Farbe */
	public String farbeHintergrund;

	/** (20) Statistik 2 */
	public String statistik2;

	/** (21) Alias */
	public String alias;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;



	/** Die Instanz des Object-Readers für die CSV-Daten */
	private static final CsvSchema csvSchema = CsvSchema.builder()
			.addColumn("kuerzel")
			.addColumn("bezeichnung")
			.addColumn("statistik1")
			.addColumn("raum")
			.addColumn("kennzeichen")
			.addColumn("farbe")
			.addNumberColumn("minWochenstunden")
			.addNumberColumn("maxWochenstunden")
			.addNumberColumn("minNachmittag")
			.addNumberColumn("maxNachmittag")
			.addColumn("fachfolgeKlasse")
			.addColumn("fachfolgeLehrkraft")
			.addColumn("fachgruppe")
			.addColumn("faktorWert")
			.addNumberColumn("faktor")
			.addColumn("text")
			.addColumn("beschreibung")
			.addColumn("farbeVordergrund")
			.addColumn("farbeHintergrund")
			.addColumn("statistik2")
			.addColumn("alias")
			.addColumn("dummy")
			.build()
			.withColumnSeparator(';')
			.withQuoteChar('\"')
			.withNullValue("")
			.withoutHeader();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UntisGPU006() {
		// leer
	}

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU006-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als Byte-Array
	 *
	 * @return eine Liste mit den GPU006-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<UntisGPU006> readCSV(final byte[] csvData) throws IOException {
		return CsvReader.fromUntis(UntisGPU006.class, csvSchema, csvData);
	}

	@Override
	public String toString() {
		return "Fach/Kurs [kuerzel=" + kuerzel + ", bezeichnung=" + bezeichnung + ", statistik1=" + statistik1 + ", raum=" + raum + ", kennzeichen="
				+ kennzeichen + ", farbe=" + farbe + ", minWochenstunden=" + minWochenstunden + ", minNachmittag=" + minNachmittag + ", maxNachmittag="
				+ maxNachmittag + ", fachfolgeKlasse=" + fachfolgeKlasse + ", fachfolgeLehrkraft=" + fachfolgeLehrkraft + ", fachgruppe=" + fachgruppe
				+ ", faktorWert=" + faktorWert + ", faktor=" + faktor + ", text=" + text + ", beschreibung=" + beschreibung + ", farbeVordergrund="
				+ farbeVordergrund + ", farbeHintergrund=" + farbeHintergrund + ", statistik2=" + statistik2 + ", alias=" + alias + ", dummy=" + dummy + "]";
	}

}
