package de.nrw.schule.svws.core.data.kursblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine
 * Kursart (z.B. 'LK') beim Blockungslagorithmus. Das Fach (z.B. 'D') zusammen
 * mit der Kursart (z.B. 'LK') definiert bei der Kursblockung eine Fachart.
 * Diese ist von Schülern wählbar. Bei der Übergabe von der GUI zum
 * Blockungsalgorithmus wird der Long-Wert der ID der Kursart in einen laufenden
 * Integer-Wert umgewandelt.
 * 
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "KursblockungInputKursart")
@Schema(name = "KursblockungInputKursart", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Kursart beim Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungInputKursart {

	/**
	 * Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei
	 * in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten
	 * aufsteigend ab 1.
	 */
	@Schema(required = true, description = "Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.", example = "1")
	public int enmRevision = -1;

	/**
	 * Die ID der Kursart. Beispielsweise der Kursart 'LK'.
	 */
	public long id;

	/**
	 * Eine String-Darstellung der Kursart, damit bei Warnungen oder Fehlern dem
	 * Benutzer diese angezeigt werden kann, beispielsweise 'LK'.
	 */
	public @NotNull String representation = "";

}
