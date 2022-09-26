package de.nrw.schule.svws.core.data.kursblockung;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen
 * Schüler beim Blockungsalgorithmus. Bei der Übergabe von der GUI zum
 * Blockungsalgorithmus wird der Long-Wert in einen laufenden Integer-Wert
 * umgewandelt.
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "KursblockungInputSchueler")
@Schema(name = "KursblockungInputSchueler", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für einen Schüler beim Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungInputSchueler {

	/**
	 * Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei
	 * in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten
	 * aufsteigend ab 1.
	 */
	@Schema(required = true, description = "Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.", example = "1")
	public int enmRevision = -1;

	/**
	 * Die ID des Schülers. Beispielsweise der Schülerin 'Mareike Mustefrau'.
	 */
	public long id;

	/**
	 * Eine String-Darstellung des Schülers, damit bei Warnungen oder Fehlern dem
	 * Benutzer diese angezeigt werden kann, beispielsweise 'Mareike Mustefrau'.
	 */
	public @NotNull String representation = "";

}
