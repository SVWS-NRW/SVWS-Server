package de.nrw.schule.svws.core.data.gost;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse ist die Core-DTO für ein Ergebnis einer Kursblockung */
@XmlRootElement
@Schema(description = "Informationen zu dem Ergebnis einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "blockungID", "name", "gostHalbjahr", "anzahlUmwaehler", "anzahlKollisionen",
		"anzahlSchienenMitKollisionen", "bewertung", "istMarkiert" })
@TranspilerDTO
public class GostBlockungsergebnisListeneintrag {
	// TODO "anzahlKollisionen", "anzahlSchienenMitKollisionen" gibt es nicht als Attribut?

	/** Die ID des Zwischenergebnisses der Blockung */
	public long id = -1;

	/** Die ID der Blockung */
	public long blockungID = -1;

	/** Der Name der Blockung */
	public @NotNull String name = "Blockung";

	/** Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	public int gostHalbjahr = 0;

	/** Die Anzahl der nötigen Umwähler bei diesem Blockungsergebnis */
	public int anzahlUmwaehler = 0;

	/** Die Bewertung dieser Blockung als numerischer Wert, welche eine automatisierte Qualitätsbewertung des
	 * Ergebnisses darstellt. */
	public long bewertung = -1;

	/** Gibt an, ob dieses Ergebnis markiert wurde. Dies kann verwendet werden, um besonders geeignete
	 * Blockungsergebnisse hervorzuheben. */
	public boolean istMarkiert = false;

	/** Bewertungskriterium 1: Array mit den Datenbank-IDs der Regeln, die nicht erfüllt werden konnten. */
	public @NotNull Long @NotNull [] bewertungNichtErfuellteRegeln = new Long[0];

	/** Bewertungskriterium 2: Anzahl aller Fachwahlen der SuS, die nicht zugeordnet wurden. */
	public long bewertungNichtZugeordneteFachwahlen = 0;

	/** Bewertungskriterium 3: Array mit dem Histogramm der Kursdifferenzen. <br>
	 * Beispiel: [7, 5, 2, 1, 0, 0, ...] bedeutet: <br>
	 * Die Kursdifferenz 0 gibt es 7 Mal <br>
	 * Die Kursdifferenz 1 gibt es 5 Mal <br>
	 * Die Kursdifferenz 2 gibt es 2 Mal <br>
	 * Die Kursdifferenz 3 gibt es 1 Mal <br>
	 */
	public @NotNull Integer @NotNull [] bewertungHistogrammDerKursdifferenzen = new Integer[0];

	/** Bewertungskriterium 4: Anzahl aller Kurse mit gleicher Fachart in einer Schiene. */
	public int bewertungGleicheFachartProSchiene = 0;

}
