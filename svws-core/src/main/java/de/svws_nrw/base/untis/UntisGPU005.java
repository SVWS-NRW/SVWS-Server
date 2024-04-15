package de.svws_nrw.base.untis;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU005.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Räume.
 */
public final class UntisGPU005 {

	/** Das Kürzel des Raumes (z.B. "TH3") */
	public String kuerzel;

	/** Die textuelle Bezeichnung des Raums (z.B. "Turnhalle 3") */
	public String bezeichnung;

	/** Das Kürzel eines Ausweichraumes (z.B. "TH2") */
	public String kuerzelAusweichraum;

	/** Kennzeichen (?) */
	public String kennzeichen;

	/** Ein leeres Feld, früher für die Farbe genutzt */
	public String leerfeld;

	/** Kennzeichen Disloz. (?) */
	public String kennzeichenDisloz;

	/** Die Gewichtung des Raumes */
	public Integer gewicht = null;

	/** Die Kapazität des Raumes */
	public Integer groesse = null;

	/** Die Abteilung zu der der Raum gehört. */
	public String abteilung;

	/** Gang 1 (?) */
	public String gang1;

	/** Gang 2 (?) */
	public String gang2;

	/** Ein Sondertext (?). */
	public String sondertext;

	/** Eine Beschreibung des Raumes (?) */
	public String beschreibung;

	/** Die Vordergrundfarbe für den Raum */
	public Integer farbeVordergrund = null;

	/** Die Hintergrundfarbe für den Raum */
	public Integer farbeHintergrund = null;

	/** Statistik 1 (?) */
	public String statistik1;

	/** Statistik 2 (?) */
	public String statistik2;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;



	/** Die Instanz des Object-Readers für die CSV-Daten */
	private static final ObjectReader reader = new CsvMapper().readerFor(UntisGPU005.class).with(CsvSchema.builder()
		.addColumn("kuerzel")
		.addColumn("bezeichnung")
		.addColumn("kuerzelAusweichraum")
		.addColumn("kennzeichen")
		.addColumn("leerfeld")
		.addColumn("kennzeichenDisloz")
		.addNumberColumn("gewicht")
		.addNumberColumn("groesse")
		.addColumn("abteilung")
		.addColumn("gang1")
		.addColumn("gang2")
		.addColumn("sondertext")
		.addColumn("beschreibung")
		.addNumberColumn("farbeVordergrund")
		.addNumberColumn("farbeHintergrund")
		.addColumn("statistik1")
		.addColumn("statistik2")
		.addColumn("dummy")
		.build()
		.withColumnSeparator(';')
		.withQuoteChar('\"')
		.withNullValue("")
		.withoutHeader()
	);

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU005-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als String
	 *
	 * @return eine Liste mit den GPU005-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<@NotNull UntisGPU005> readCSV(final String csvData) throws IOException {
		try (MappingIterator<UntisGPU005> it = reader.readValues(csvData)) {
			return it.readAll();
		}
	}

	@Override
	public String toString() {
		return "Raum [kuerzel=" + kuerzel + ", bezeichnung=" + bezeichnung + ", groesse=" + groesse + "]";
	}

}
