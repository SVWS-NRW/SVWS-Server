package de.nrw.schule.svws.core.data.kursblockung;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die
 * Sammlung mehrerer Ergebnisse einer Blockung. Die GUI erhält diese Daten und
 * interpretiert sie.
 */
@XmlRootElement(name = "KursblockungOutputs")
@Schema(name = "KursblockungOutputs", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Ouput-Daten des Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungOutputs {

	/**
	 * Die Revision der Kursblockungs-Ausgabedaten, um zu überprüfen, ob die Datei
	 * in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten
	 * aufsteigend ab 1.
	 */
	@Schema(required = true, description = "Die Revision der Kursblockungs-Ausgabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.", example = "1")
	public int blockungsRevision = -1;

	/**
	 * Rückgabe mehrerer Blockungsergebnisse.
	 */
	public @NotNull Vector<@NotNull KursblockungOutput> outputs = new Vector<>();

}
