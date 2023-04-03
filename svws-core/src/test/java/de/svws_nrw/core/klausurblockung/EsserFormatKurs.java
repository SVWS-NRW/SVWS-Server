package de.svws_nrw.core.klausurblockung;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle {@code kurs.txt} eines Datensatzes einer Klausurplanung im
 * Format von Mirko Esser. In dieser Datei findet man alle Kurse.
 *
 * @author Benjamin A. Bartsch
 */
@JsonPropertyOrder({ "id", "stufe", "kursart", "fach", "kursnr", "lehrer", "halbjahr" })
public class EsserFormatKurs {

	/** Die ID des Datensatzes. **/
	@JsonProperty public int id;

	/** Die Klassenstufe (EF, Q1, Q2) des Kurses. **/
	@JsonProperty public String stufe;

	/** Die Kursart (LK, GK, ...) des Kurses. **/
	@JsonProperty public String kursart;

	/** Das Fach (D, E, M, ...) des Kurses. **/
	@JsonProperty public String fach;

	/** Die Kursnummer des Kurses. Das ist der Suffix von D-GK1, D-GK2, ... . **/
	@JsonProperty public int kursnr;

	/** Das Kürzel der Lehrkraft des Kurses. **/
	@JsonProperty public String lehrer;

	/** Das Halbjahr zu dem der Kurs gehört. **/
	@JsonProperty public int halbjahr;

}
