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
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU015.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Kurswahlen der Studenten, d.h. der Schüler-Kurs-Zuordnungen.
 */
public final class UntisGPU015 {

	/** Der Name des Schülers (z.B. "Adam_Tho_20031119") */
	public String name;

	/** Die Unterrichtsnummer (siehe GPU002) */
	public Long idUnterricht = null;

	/** Das Kürzel des Faches oder des Kurses (z.B. "M-GK2") */
	public String fach;

	/** Das Alias des Unterrichtes */
	public String unterrichtAlias;

	/** Die Klasse des Schülers */
	public String klasse;

	/** Das Statistikkennzeichen ("M" - mündlich, "S" - schriftlich) */
	public String statistikKennzeichen;

	/** Die Nummer des Schülers (nur bei Export) */
	public String stundentennummer;

	/** Reserviert */
	public String reserviert1;

	/** Reserviert */
	public String reserviert2;

	/** Unterrichtsnummern der Alternativekurse (mit ~ getrennt, 4035~4036~4037) */
	public String idsUnterrichteAlternativkurse;

	/** Kürzel der Alternativkurse (mit ~ getrennt, z.B. "M-GK1, M-GK2, M-GK3") */
	public String kuerzelAlternativkurse;

	/** Reserviert */
	public String reserviert3;

	/** Prioritäten der Alternativkurse (mit ~ getrennt, z.B. "1~1~1") */
	public String prioAlternativkurse;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;


	/** Das CSV-Schema */
	private static final CsvSchema csvSchema = CsvSchema.builder()
			.addColumn("name")
			.addNumberColumn("idUnterricht")
			.addColumn("fach")
			.addColumn("unterrichtAlias")
			.addColumn("klasse")
			.addColumn("statistikKennzeichen")
			.addColumn("stundentennummer")
			.addColumn("reserviert1")
			.addColumn("reserviert2")
			.addColumn("idsUnterrichteAlternativkurse")
			.addColumn("kuerzelAlternativkurse")
			.addColumn("reserviert3")
			.addColumn("prioAlternativkurse")
			.addColumn("dummy")
			.build()
			.withColumnSeparator(';')
			.withQuoteChar('\"')
			.withNullValue("")
			.withoutHeader();


	/** Die Instanz des Object-Writers für die CSV-Daten */
	private static final ObjectWriter writer = new CsvMapper().writerFor(UntisGPU015.class).with(csvSchema).with(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UntisGPU015() {
		// leer
	}

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU015-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als Byte-Array
	 *
	 * @return eine Liste mit den GPU015-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<UntisGPU015> readCSV(final byte[] csvData) throws IOException {
		return CsvReader.fromUntis(UntisGPU015.class, csvSchema, csvData);
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
	public static String writeCSV(final @NotNull List<UntisGPU015> dtos) throws IOException {
		final StringWriter sw = new StringWriter();
		writer.writeValues(sw).writeAll(dtos).close();
		return sw.toString();
	}


	@Override
	public String toString() {
		return "Unterrichtsfolge [name=" + name + ", idUnterricht=" + idUnterricht + ", fach=" + fach + ", klasse=" + klasse
				+ ", schriftlichkeit=" + statistikKennzeichen + ", idsUnterrichteAlternativkurse=" + idsUnterrichteAlternativkurse
				+ ", kuerzelAlternativkurse=" + kuerzelAlternativkurse + ", prioAlternativkurse=" + prioAlternativkurse + "]";
	}

}
