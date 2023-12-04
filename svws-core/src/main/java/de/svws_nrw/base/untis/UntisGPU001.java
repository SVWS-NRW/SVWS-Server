package de.svws_nrw.base.untis;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU001.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung des
 * Stundenplans.
 */
public final class UntisGPU001 {

	/** Eine numerische ID, welche den Unterricht eindeutig identifiziert (z.B. 42) */
	public long idUnterricht;

	/** Das Kürzel der Klasse (z.B. "05A") */
	public String klasseKuerzel;

	/** Das Kürzel des Lehrers (z.B. "BACH") */
	public String lehrerKuerzel;

	/** Das Kürzel des Fachen (z.B. "D") */
	public String fachKuerzel;

	/** Das Kürzel des Raumes (z.B. "") */
	public String raumKuerzel;

	/** Der Wochentag für den Unterricht */
	public int wochentag;

	/** Die Stunde im Zeitraster des Wochentags */
	public int stunde;

	/** Ggf. die Stundenlänge in Minuten oder leer */
	public Integer dauer = null;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;



	/** Die Instanz des Object-Readers für die CSV-Daten */
	private static final ObjectReader reader = new CsvMapper().readerFor(UntisGPU001.class).with(CsvSchema.builder()
		.addNumberColumn("idUnterricht")
		.addColumn("klasseKuerzel")
		.addColumn("lehrerKuerzel")
		.addColumn("fachKuerzel")
		.addColumn("raumKuerzel")
		.addNumberColumn("wochentag")
		.addNumberColumn("stunde")
		.addNumberColumn("dauer")
		.addColumn("dummy")
		.build()
		.withColumnSeparator(';')
		.withQuoteChar('\"')
		.withNullValue("")
		.withoutHeader()
	);

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU001-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als String
	 *
	 * @return eine Liste mit den GPU001-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<@NotNull UntisGPU001> readCSV(final String csvData) throws IOException {
		try (MappingIterator<UntisGPU001> it = reader.readValues(csvData)) {
			return it.readAll();
		}
	}

	@Override
	public String toString() {
		return "Unterricht[id=" + idUnterricht + ", klasse=" + klasseKuerzel + ", lehrer="
				+ lehrerKuerzel + ", fach/kurs=" + fachKuerzel + ", raum=" + raumKuerzel + ", wochentag="
				+ wochentag + ", stunde=" + stunde + "]";
	}

}
