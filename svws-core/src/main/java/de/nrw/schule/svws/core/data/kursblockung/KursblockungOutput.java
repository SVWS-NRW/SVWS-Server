package de.nrw.schule.svws.core.data.kursblockung;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für das Ergebnis einer Blockung. Es beinhaltet
 * die Zuordnung der SuS auf ihre Kurse sowie die Zuordnung der Kurse auf die Schienen. Die GUI erhält diese Daten und
 * interpretiert sie. */
@XmlRootElement(name = "KursblockungOutput")
@Schema(name = "KursblockungOutput", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Ouput-Daten des Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungOutput {

	/** Die Revision der Kursblockungs-Ausgabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1
	 * für Entwickler-Revisionen und ansonsten aufsteigend ab 1. */
	@Schema(required = true, description = "Die Revision der Kursblockungs-Ausgabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1).", example = "1")
	public int blockungsRevision = -1;

	/** Der konkrete Seed, welcher vom Blockungs-Algorithmus verwendet wurde. Hilfreich zur Analyse von potentiellen
	 * Fehlern. So kann man den Algorithmus mit dem selben Seed erneut starten, um den Fehler zu reproduzieren. */
	public long seed = 0;

	/** Die ID des zugehörigen Eingabe-Objekts. So lässt sich diese Ausgabe der richtigen Eingabe zuordnen. Die ID
	 * stammt von der GUI bzw. der Datenbank. */
	public long input = 0;

	/** Die Zuordnung der Kurse auf ihre Schienen. */
	public @NotNull Vector<@NotNull KursblockungOutputKursZuSchiene> kursZuSchiene = new Vector<>();

	/** Alle Fachwahlen. Diese enthalten den zugeordneten Kurs oder, ob keiner zugeordnet wurde. */
	public @NotNull Vector<@NotNull KursblockungOutputFachwahlZuKurs> fachwahlenZuKurs = new Vector<>();

	/** Bewertungskriterium 1: Array mit den IDs der Regeln, die nicht erfüllt werden konnten. */
	@Schema(required = true, description = "Bewertungskriterium 1: Array mit den IDs der Regeln, die nicht erfüllt werden konnten.", example = "[1,2,7]")
	// habe mich gegen @ArraySchema entschieden, da man keine Beschreibung hinzufügen kann.
	public @NotNull Long @NotNull [] bewertungNichtErfuellteRegeln = new Long[0];

	/** Bewertungskriterium 2: Anzahl aller Fachwahlen der SuS, die nicht zugeordnet wurden. */
	@Schema(required = true, description = "Bewertungskriterium 2: Anzahl aller Fachwahlen der SuS, die nicht zugeordnet wurden.", example = "5")
	public long bewertungNichtZugeordneteFachwahlen = 0;

	/** Bewertungskriterium 3: Array mit dem Histogramm der Kursdifferenzen. <br>
	 * Beispiel: [7, 5, 2, 1, 0, 0, ...] bedeutet: <br>
	 * Die Kursdifferenz 0 gibt es 7 Mal <br>
	 * Die Kursdifferenz 1 gibt es 5 Mal <br>
	 * Die Kursdifferenz 2 gibt es 2 Mal <br>
	 * Die Kursdifferenz 3 gibt es 1 Mal <br>
	 */
	@Schema(required = true, description = "Bewertungskriterium 3: Array mit dem Histogramm der Kursdifferenzen. Beispiel: [7, 5, 2, 1, 0, 0, ...] bedeutet: <br> Die Kursdifferenz 0 gibt es 7 Mal <br> Die Kursdifferenz 1 gibt es 5 Mal <br> Die Kursdifferenz 2 gibt es 2 Mal <br> Die Kursdifferenz 3 gibt es 1 Mal <br>", example = "[7, 5, 2, 1, 0, 0, ...]")
	// habe mich gegen @ArraySchema entschieden, da man keine Beschreibung hinzufügen kann.
	public @NotNull Integer @NotNull [] bewertungHistogrammDerKursdifferenzen = new Integer[0];

	/** Bewertungskriterium 4: Anzahl aller Kurse mit gleicher Fachart in einer Schiene. */
	@Schema(required = true, description = "Bewertungskriterium 4: Anzahl aller Kurse mit gleicher Fachart in einer Schiene.", example = "11")
	public int bewertungGleicheFachartProSchiene = 0;

}
