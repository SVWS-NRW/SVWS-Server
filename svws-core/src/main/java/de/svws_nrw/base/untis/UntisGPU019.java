package de.svws_nrw.base.untis;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

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
	public Integer idUnterricht = null;

	/** Das Alias des Faches */
	public String fachAlias;

	/** Das Kürzel des Faches oder des Kurses (z.B. "M-GK2") */
	public String fach;

	/** Die zugehörigen Klassen (separiert mit ~, z.B. "EF") */
	public String klassen;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;



	/** Die Instanz des Object-Readers für die CSV-Daten */
	private static final ObjectReader reader = new CsvMapper().readerFor(UntisGPU019.class).with(CsvSchema.builder()
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
		.withoutHeader()
	);

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU019-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als String
	 *
	 * @return eine Liste mit den GPU019-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<@NotNull UntisGPU019> readCSV(final String csvData) throws IOException {
		try (MappingIterator<UntisGPU019> it = reader.readValues(csvData)) {
			return it.readAll();
		}
	}

	@Override
	public String toString() {
		return "Unterrichtsfolge [name=" + name + ", art=" + art + ", anzahlWochenstunden=" + anzahlWochenstunden
			+ ", idUnterricht=" + idUnterricht + ", fachAlias=" + fachAlias + ", fach=" + fach + ", klassen=" + klassen + "]";
	}

}
