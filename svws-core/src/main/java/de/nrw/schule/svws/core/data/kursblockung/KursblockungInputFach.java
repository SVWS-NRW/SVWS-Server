package de.nrw.schule.svws.core.data.kursblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für ein
 * Fach (z.B. 'D') für den Blockungsalgorithmus. Das Fach zusammen mit der
 * Kursart wird als Fachart bezeichnet. Eine Fachart ist von Schülern wählbar.
 * Bei der Übergabe von der GUI zum Blockungsalgorithmus wird der Long-Wert der
 * ID in einen laufenden Integer-Wert umgewandelt.
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "KursblockungInputFach")
@Schema(name = "KursblockungInputFach", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für das Fach beim Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungInputFach {

	/**
	 * Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei
	 * in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten
	 * aufsteigend ab 1.
	 */
	@Schema(required = true, description = "Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.", example = "1")
	public int enmRevision = -1;

	/**
	 * Die ID des Faches. Beispielsweise des Faches 'D'.
	 */
	public long id;

	/**
	 * Eine String-Darstellung des Faches, damit bei Warnungen oder Fehlern dem
	 * Benutzer diese angezeigt werden kann.
	 */
	public @NotNull String representation = "";

}
