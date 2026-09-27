package de.svws_nrw.core.klausurblockung;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Diese Klasse dient als DTO für die CSV-Tabelle {@code kurs.txt} eines Datensatzes einer Klausurplanung. In dieser Datei findet man alle SuS.
 *
 * @author Benjamin A. Bartsch */
@JsonPropertyOrder({ "id", "kursid", "stufe", "vorname", "nachname" })
public class KlausurplanFormatSchueler {

	/** Die ID des Datensatzes. **/
	@JsonProperty
	public int id;

	/** Die ID bei einem externen Programm. **/
	@JsonProperty
	public int kursid;

	/** Die Stufe in der die Person gerade ist. **/
	@JsonProperty
	public String stufe;

	/** Der Vorname der Person. **/
	@JsonProperty
	public String vorname;

	/** Der Nachname. **/
	@JsonProperty
	public String nachname;

}
