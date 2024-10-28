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
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU019.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Unterrichtsfolgen.
 */
public final class UntisGPU019 {

	/** Der Name der Unterrichtsfolge (z.B. "Schiene 1") */
	public String name;

	/** Die Art der Unterrichtsfolge ("1" - fixe Unterrichtsfolge, "2" - Gleichzeitiger Unterricht/Band/Schiene, "3" - Wochenfolge) */
	public String art;

	/** Die Anzahl der Wochenstunden (z.B. 3) */
	public Integer anzahlWochenstunden = null;

	/** Die Unterrichtsnummer (siehe GPU002) */
	public Long idUnterricht = null;

	/** Das Alias des Faches */
	public String fachAlias;

	/** Das Kürzel des Faches oder des Kurses (z.B. "M-GK2") */
	public String fach;

	/** Die zugehörigen Klassen (separiert mit ~, z.B. "EF") */
	public String klassen;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;



	/** Das CSV-Schema */
	private static final CsvSchema csvSchema = CsvSchema.builder()
			.addColumn("name")
			.addColumn("art")
			.addNumberColumn("anzahlWochenstunden")
			.addNumberColumn("idUnterricht")
			.addColumn("fachAlias")
			.addColumn("fach")
			.addColumn("klassen")
			.addColumn("dummy")
			.build()
			.withColumnSeparator(';')
			.withQuoteChar('\"')
			.withNullValue("")
			.withoutHeader();

	/** Die Instanz des Object-Writers für die CSV-Daten */
	private static final ObjectWriter writer = new CsvMapper().writerFor(UntisGPU019.class).with(csvSchema).with(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UntisGPU019() {
		// leer
	}

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU019-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als Byte-Array
	 *
	 * @return eine Liste mit den GPU019-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<UntisGPU019> readCSV(final byte[] csvData) throws IOException {
		return CsvReader.fromUntis(UntisGPU019.class, csvSchema, csvData);
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
	public static String writeCSV(final @NotNull List<UntisGPU019> dtos) throws IOException {
		final StringWriter sw = new StringWriter();
		writer.writeValues(sw).writeAll(dtos).close();
		return sw.toString();
	}

	@Override
	public String toString() {
		return "Unterrichtsfolge [name=" + name + ", art=" + art + ", anzahlWochenstunden=" + anzahlWochenstunden
				+ ", idUnterricht=" + idUnterricht + ", fachAlias=" + fachAlias + ", fach=" + fach + ", klassen=" + klassen + "]";
	}

}
