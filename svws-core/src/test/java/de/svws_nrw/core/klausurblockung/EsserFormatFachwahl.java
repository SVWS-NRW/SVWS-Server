package de.svws_nrw.core.klausurblockung;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Diese Klasse dient als DTO für die CSV-Tabelle {@code kurs.txt} eines Datensatzes einer Klausurplanung im Format von
 * Mirko Esser. In dieser Datei findet man alle Fachwahlen der SuS.
 * 
 * @author Benjamin A. Bartsch */
@JsonPropertyOrder({ "kurs", "schueler", "schriftlich" })
public class EsserFormatFachwahl {

	/** Fremdschlüssel: Die ID des Kurses. **/
	@JsonProperty public int kurs;

	/** Fremdschlüssel: Die ID des Schülers. **/
	@JsonProperty public int schueler;

	/** Die Art der Fachwahl. Werte größer 1 entsprechend einer schriftlichen Wahl: <br>
	 * 1 = mündlich, 2 = schriftlich (GK), 3 = Abifach 3, 4 = Abifach 4, 5 = Leistungskurs **/
	@JsonProperty public int schriftlich;

}