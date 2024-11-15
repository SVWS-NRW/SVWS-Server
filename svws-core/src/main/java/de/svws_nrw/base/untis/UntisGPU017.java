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
 * Diese Klasse dient als DTO für die CSV-Datei {@code GPU017.txt} des
 * Untis-Datenaustausch-Formates und enthält damit eine Beschreibung der
 * Klausuren.
 */
public final class UntisGPU017 {

	/** Der Name der Klausur (z.B. "LK1_1") */
	public String name;

	/** Die ID der Klausur */
	public Long id = null;

	/** Der Text der Klausur (z.B. "LK Schiene 1 - 1. Quartal") */
	public String text;

	/** Das Datum der Klausur (JJJJMMTT) */
	public Long datum = null;

	/** Die erste Stunde der Klausur */
	public Long vonStunde;

	/** Die letzte Stunde der Klausur */
	public Long bisStunde;

	/** Die von der Klausur betroffenen Kurse (z.B. "KU-GK1~KU-GK2") */
	public String kurse;

	/** Die von der Klausur betroffenen Unterrichtsnummern (z.B. "543~544~545") */
	public String unterrichte;

	/** Die von der Klausur betroffenen Schüler (z.B. "Mustermann_Max_20320229~Mustermann_Erika_20320229") */
	public String schueler;

	/** Die bei der Klausur Aufsicht führenden Lehrer für die einzelnen Stunden. Die Stunden werden mit " - " getrennt. Die Lehrer werden in jeder Stunde mit "~" getrennt. */
	public String lehrer;

	/** Die Klausurräume für die einzelnen Stunden. Die Stunden werden mit " - " getrennt. Die Räume werden in jeder Stunde mit "~" getrennt */
	public String raeume;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	public String dummy;


	/** Das CSV-Schema */
	private static final CsvSchema csvSchema = CsvSchema.builder()
			.addColumn("name")
			.addNumberColumn("id")
			.addColumn("text")
			.addNumberColumn("datum")
			.addNumberColumn("vonStunde")
			.addNumberColumn("bisStunde")
			.addColumn("kurse")
			.addColumn("unterrichte")
			.addColumn("schueler")
			.addColumn("lehrer")
			.addColumn("raeume")
			.addColumn("dummy")
			.build()
			.withColumnSeparator(';')
			.withQuoteChar('\"')
			.withNullValue("")
			.withoutHeader();


	/** Die Instanz des Object-Writers für die CSV-Daten */
	private static final ObjectWriter writer = new CsvMapper().writerFor(UntisGPU017.class).with(csvSchema).with(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UntisGPU017() {
		// leer
	}

	/**
	 * Erstellt aus den übergebenen CSV-Daten eine Liste der GPU017-Datensätze
	 *
	 * @param csvData   die Daten des CSV-Datei als Byte-Array
	 *
	 * @return eine Liste mit den GPU017-Datensätzen
	 *
	 * @throws IOException falls die CSV-Datei nicht korrekt gelesen werden kann
	 */
	public static @NotNull List<UntisGPU017> readCSV(final byte[] csvData) throws IOException {
		return CsvReader.fromUntis(UntisGPU017.class, csvSchema, csvData);
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
	public static String writeCSV(final @NotNull List<UntisGPU017> dtos) throws IOException {
		final StringWriter sw = new StringWriter();
		writer.writeValues(sw).writeAll(dtos).close();
		return sw.toString();
	}


	@Override
	public String toString() {
		return "Klausuren [name=" + name + ", id=" + id + ", text=" + text + ", datum=" + datum + ", vonStunde=" + vonStunde + ", bisStunde=" + bisStunde
				+ ", kurse=" + kurse + ", unterrichte=" + unterrichte + ", schueler=" + schueler + ", lehrer=" + lehrer + ", raeume=" + raeume + ", dummy="
				+ dummy + "]";
	}

}
