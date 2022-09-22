package de.nrw.schule.svws.core.klausurblockung;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Diese Klasse dient als DTO für die CSV-Tabelle {@code kdata.txt} eines Datensatzes einer Klausurplanung im Format
 * von Mirko Esser. In dieser Datei findet man die Zuordnungen der Klausuren zu Quartalen.
 * 
 * @author Benjamin A. Bartsch */
@JsonPropertyOrder({ "id", "fach", "kursart", "stufe", "halbjahr", "klausnr", "dauer", "zeitdiff", "bemerkung" })
public class EsserFormatKData {

	/** Die ID des Datensatzes. **/
	@JsonProperty public int id;

	/** Der Name des Faches. */
	@JsonProperty public String fach;

	/** Der Name der Kursart. */
	@JsonProperty public String kursart;

	/** Der Name der Stufe. */
	@JsonProperty public String stufe;

	/** Das Halbjahr in dem die Klausur geschrieben wird. **/
	@JsonProperty public int halbjahr;

	/** Die Nummer der Klausur im Halbjahr. **/
	@JsonProperty public int klausnr;

	/** Zusatzzeit Zeit für die Klausur. **/
	@JsonProperty public int zeitdiff;

	/** Die Dauer der Klausur in Minuten. **/
	@JsonProperty public int dauer;

	/** Weitere Bemerkungen. Kann auch den Wert "NULL" enthalten. **/
	@JsonProperty public String bemerkung;

}