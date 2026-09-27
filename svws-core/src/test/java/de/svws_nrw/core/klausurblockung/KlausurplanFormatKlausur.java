package de.svws_nrw.core.klausurblockung;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Diese Klasse dient als DTO für die CSV-Tabelle {@code kdata.txt} eines Datensatzes einer Klausurplanung. In dieser Datei findet man die Zuordnungen der Klausuren zu Kursen.
 *
 * @author Benjamin A. Bartsch */
@JsonPropertyOrder({ "id", "kurs", "kdata", "termin" })
public class KlausurplanFormatKlausur {

	/** Die ID des Datensatzes. **/
	@JsonProperty
	public int id;

	/** Fremdschlüssel: Referenz zur Kurs-Tabelle. **/
	@JsonProperty
	public int kurs;

	/** Fremdschlüssel: Referenz zur KData-Tabelle. **/
	@JsonProperty
	public int kdata;

	/** Fremdschlüssel: Referenz zur Termin-Tabelle (nicht vorhanden). **/
	@JsonProperty
	public int termin;

}
