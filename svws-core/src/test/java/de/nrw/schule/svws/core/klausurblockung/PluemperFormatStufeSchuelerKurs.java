package de.nrw.schule.svws.core.klausurblockung;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Diese Klasse dient als DTO für die CSV-Tabelle {@code kurs.txt} eines Datensatzes einer Klausurplanung im Format von
 * Michael Plümper. In dieser Datei findet man alle Tripel aus [Stufen, Schüler, Kurse].
 * 
 * @author Benjamin A. Bartsch */
@JsonPropertyOrder({ "stufe", "schuelerid", "kursid" })
public class PluemperFormatStufeSchuelerKurs {

	/** Die Klassenstufe (EF, Q1, Q2) des Kurses. **/
	@JsonProperty public String stufe;

	/** Die ID des Schülers. **/
	@JsonProperty public long schuelerid;

	/** Die ID des Kurses. **/
	@JsonProperty public long kursid;

}