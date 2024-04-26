package de.svws_nrw.base.untis;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
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



	/** Die Instanz des Object-Readers für die CSV-Daten */
	private static final ObjectReader reader = new CsvMapper().readerFor(UntisGPU010.class).with(CsvSchema.builder()
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
		.withoutHeader()
	);

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

	@Override
	public String toString() {
		return "Unterrichtsfolge [name=" + name + ", langname=" + langname + ", vorname=" + vorname + ", fremdschluessel=" + fremdschluessel
			+ ", klasse=" + klasse + ", geschlecht=" + geschlecht + ", geburtsdatum=" + geburtsdatum + ", emailAdresse=" + emailAdresse + "]";
	}

}
