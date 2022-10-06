package de.nrw.schule.svws.core.data.gost;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die Core-DTO für die Bewertung eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description="Bewertung eines Ergebnis einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "regelVerletzungen", "anzahlNichtZugeordnet", "anzahlKollisionen", "anzahlSchienenMitKollisionen",
	"kursdifferenzen", "anzahlKurseMitGleicherFachartProSchiene" })
@TranspilerDTO
public class GostBlockungsergebnisBewertung {

	/** Bewertungskriterium 1: Array mit den Datenbank-IDs der Regeln, die nicht erfüllt werden konnten. */
	public @NotNull Long @NotNull [] regelVerletzungen = new Long[0];

	/** Bewertungskriterium 2a: Anzahl aller Fachwahlen der SuS, die nicht zugeordnet wurden. */
	public long anzahlNichtZugeordnet = 0;

	/** Bewertungskriterium 2b: Anzahl der Kollisionen bei der Zuordnung von Schülern zu den Kurses in den Schienen. */
	public long anzahlKollisionen = 0;

	/** Bewertungskriterium 3: Array mit dem Histogramm der Kursdifferenzen. <br>
	 * Beispiel: [7, 5, 2, 1, 0, 0, ...] bedeutet: <br>
	 * Die Kursdifferenz 0 gibt es 7 Mal <br>
	 * Die Kursdifferenz 1 gibt es 5 Mal <br>
	 * Die Kursdifferenz 2 gibt es 2 Mal <br>
	 * Die Kursdifferenz 3 gibt es 1 Mal <br>
	 */
	public @NotNull Integer @NotNull [] kursdifferenzen = new Integer[0];

	/** Bewertungskriterium 4: Anzahl aller Kurse mit gleicher Fachart in einer Schiene. */
	public int anzahlKurseMitGleicherFachartProSchiene = 0;

}
