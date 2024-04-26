package de.svws_nrw.base.untis;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU015.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Kurswahlen der Stundenten, d.h. der Schüler-Kurs-Zuordnungen.
 */
public final class UntisGPU015 {

	/** Der Name des Schülers (z.B. "Adam_Tho_20031119") */
	public String name;

	/** Die Unterrichtsnummer (siehe GPU002) */
	public Integer idUnterricht = null;

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



	/** Die Instanz des Object-Readers für die CSV-Daten */
	private static final ObjectReader reader = new CsvMapper().readerFor(UntisGPU015.class).with(CsvSchema.builder()
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
		.withoutHeader()
	);

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU015-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als String
	 *
	 * @return eine Liste mit den GPU015-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<@NotNull UntisGPU015> readCSV(final String csvData) throws IOException {
		try (MappingIterator<UntisGPU015> it = reader.readValues(csvData)) {
			return it.readAll();
		}
	}

	@Override
	public String toString() {
		return "Unterrichtsfolge [name=" + name + ", idUnterricht=" + idUnterricht + ", fach=" + fach + ", klasse=" + klasse
			+ ", schriftlichkeit=" + statistikKennzeichen + ", idsUnterrichteAlternativkurse=" + idsUnterrichteAlternativkurse
			+ ", kuerzelAlternativkurse=" + kuerzelAlternativkurse + ", prioAlternativkurse=" + prioAlternativkurse + "]";
	}

}
