package de.svws_nrw.core.data.gost;

import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.transpiler.TranspilerDTO;
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

	/** Bewertungskriterium 1a: Array mit den Regel-IDs der {@link GostBlockungRegel} die nicht erfüllt werden konnten. */
	public @NotNull Vector<@NotNull Long> regelVerletzungen = new Vector<>();

	/** Bewertungskriterium 1b: Anzahl aller Kurse, die nicht auf Schienen verteilt wurden. */
	public int anzahlKurseNichtZugeordnet = 0;
	
	/** Bewertungskriterium 2a: Anzahl aller Fachwahlen der SuS, die nicht zugeordnet wurden. */
	public int anzahlSchuelerNichtZugeordnet = 0;

	/** Bewertungskriterium 2b: Anzahl der Kollisionen bei der Zuordnung von Schülern zu den Kurses in den Schienen. */
	public int anzahlSchuelerKollisionen = 0;


	/** Bewertungskriterium 3a: Die größte Kursdifferenz in der Blockung. */
	public int kursdifferenzMax = 0;
	
	/** Bewertungskriterium 3b: Array mit dem Histogramm der Kursdifferenzen. <br>
	 * Beispiel: [7, 5, 2, 1, 0, 0, ...] bedeutet: <br>
	 * Die Kursdifferenz 0 gibt es 7 Mal <br>
	 * Die Kursdifferenz 1 gibt es 5 Mal <br>
	 * Die Kursdifferenz 2 gibt es 2 Mal <br>
	 * Die Kursdifferenz 3 gibt es 1 Mal <br>
	 */
	public @NotNull int [] kursdifferenzHistogramm = new int[0];

	/** Bewertungskriterium 4: Anzahl aller Kurse mit gleicher Fachart in einer Schiene. */
	public int anzahlKurseMitGleicherFachartProSchiene = 0;

}
