package de.svws_nrw.core.stundenplan;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Diese Klasse dient als DTO für CSV-Tabellen im "Schulmanager" - Format,
 * welche vom Programm WinStupas (Version 3.9.14) generiert werden kann.
 *
 * @author Benjamin A. Bartsch
 */
@JsonPropertyOrder({ "Id", "KursId", "Art", "Lehrerkuerzel", "Fach", "Kurs", "Raum", "Wochentag", "Stunde", "Bezeichnung", "Woche", "Klassen", "Kopplung" })
public class StupasSchulmanagerFormatLine {

	/** Eine ID der Zeile des Datensatzes.*/
	public int Id;

	/** Eine ID des Unterrichts des Datensatzes.*/
	public int KursId;

	/** Die Art des Datensatzes, z.B. "Stunde". */
	public String Art;

	/** Das Kürzel der Lehrkraft des Unterrichts. */
	public String Lehrerkuerzel;

	/** Das Fach des Unterrichts. */
	public String Fach;

	/** Ein potentieller Kurs-Suffix. */
	public String Kurs;

	/** Das Kürzel des Raumes des Unterrichts. */
	public String Raum;

	/** Der Wochentag (Montag=1 bis Freitag=7) des Unterrichts. */
	public int Wochentag;

	/** Die Unterrichtsstunde (1 bis 16) des Unterrichts. */
	public int Stunde;

	/** Dummy. */
	public String Bezeichnung;

	/** Die Woche (A-Woche=1, B-Woche=2, ...) oder 0 (jede Woche). */
	public int Woche;

	/** Alle am Unterricht beteiligten Klassen. */
	public String Klassen;

	/** Die potentielle Kopplung des Unterrichts. */
	public String Kopplung;

}
