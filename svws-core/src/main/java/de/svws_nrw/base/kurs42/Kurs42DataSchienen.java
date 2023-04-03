package de.svws_nrw.base.kurs42;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle {@code Schienen.txt} eines Kurs42-Textdatei-Exportes.
 *
 * @author Thomas Bachran
 */
@JsonPropertyOrder({ "Id", "Bezeichnung", "KopplungsId", "Stundenplan" })
public class Kurs42DataSchienen {

	/** Die ID der Schiene innerhalb von Kurs 42. Entspricht vermutlich der Schienen-Nummer (hier 1-indiziert) */
	public int Id;

	/** Die Bezeichnung der Schiene. */
	public String Bezeichnung;

	/** KopplungsId - beim Import icht verwendet */
	public String KopplungsId;

	/** Stundenplan - beim Import nicht verwendet */
	public String Stundenplan;

}
