package de.svws_nrw.base.untis;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU010.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Stundenten bzw. Schüler.
 */
public final class UntisGPU010 {

	/** Der Name des Schülers (z.B. "Adam_Tho_20031119") */
	public String name;

	/** Der Langname (z.B. der Nachname "Adam") */
	public String langname;

	/** Ein Text */
	public String text;

	/** Eine Beschreibung */
	public String beschreibung;

	/** Statistik-Feld 1 */
	public String statistik1;

	/** Statistik-Feld 2 */
	public String statistik2;

	/** Ein Kennzeichen */
	public String kennzeichen;

	/** Der Vorname des Schülers (z.B. "Thomas") */
	public String vorname;

	/** Eine Nummer des Schülers (z.B. "42") */
	public String schuelernummer;

	/** Die Klasse des Schülers */
	public String klasse;

	/** Das Geschlecht des Schüler (1 = weiblich, 2 = männlich) */
	public String geschlecht;

	/** Ein (Kurs-)Optimierungskennzeichen */
	public String optimierungskennzeichen;

	/** Das Geburtsdatum JJJJMMTT */
	public String geburtsdatum;

	/** Die E-Mail Adresse */
	public String emailAdresse;

	/** Ein Fremdschlüssel / externe ID (z.B. "1001") */
	public String fremdschluessel;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;


	/** Das CSV-Schema */
	private static final CsvSchema schema = CsvSchema.builder()
		.addColumn("name")
		.addColumn("langname")
		.addColumn("text")
		.addColumn("beschreibung")
		.addColumn("statistik1")
		.addColumn("statistik2")
		.addColumn("kennzeichen")
		.addColumn("vorname")
		.addColumn("schuelernummer")
		.addColumn("klasse")
		.addColumn("geschlecht")
		.addColumn("optimierungskennzeichen")
		.addColumn("geburtsdatum")
		.addColumn("emailAdresse")
		.addColumn("fremdschluessel")
		.addColumn("dummy")
		.build()
		.withColumnSeparator(';')
		.withQuoteChar('\"')
		.withNullValue("")
		.withoutHeader();

	/** Die Instanz des Object-Readers für die CSV-Daten */
	private static final ObjectReader reader = new CsvMapper().readerFor(UntisGPU010.class).with(schema);

	/** Die Instanz des Object-Writers für die CSV-Daten */
	private static final ObjectWriter writer = new CsvMapper().writerFor(UntisGPU010.class).with(schema).with(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS);


	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU010-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als String
	 *
	 * @return eine Liste mit den GPU010-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<@NotNull UntisGPU010> readCSV(final String csvData) throws IOException {
		try (MappingIterator<UntisGPU010> it = reader.readValues(csvData)) {
			return it.readAll();
		}
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
	public static String writeCSV(final @NotNull List<@NotNull UntisGPU010> dtos) throws IOException {
		final StringWriter sw = new StringWriter();
		try (SequenceWriter seqw = writer.writeValues(sw).writeAll(dtos)) {
			return sw.toString();
		}
	}


	@Override
	public String toString() {
		return "Unterrichtsfolge [name=" + name + ", langname=" + langname + ", vorname=" + vorname + ", fremdschluessel=" + fremdschluessel
			+ ", klasse=" + klasse + ", geschlecht=" + geschlecht + ", geburtsdatum=" + geburtsdatum + ", emailAdresse=" + emailAdresse + "]";
	}

}
