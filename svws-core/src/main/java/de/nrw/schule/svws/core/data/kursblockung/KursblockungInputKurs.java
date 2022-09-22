package de.nrw.schule.svws.core.data.kursblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen
 * Kurs beim Blockungsalgorithmus. Bei der Übergabe von der GUI zum
 * Blockungsalgorithmus wird der Long-Wert in einen laufenden Integer-Wert
 * umgewandelt.
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "KursblockungInputKurs")
@Schema(name = "KursblockungInputKurs", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für einen Kurs beim Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungInputKurs {

	/**
	 * Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei
	 * in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten
	 * aufsteigend ab 1.
	 */
	@Schema(required = true, description = "Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.", example = "1")
	public int enmRevision = -1;

	/**
	 * Die ID des Kurses. Beispielsweise des Kurses 'D-LK1'.
	 */
	public long id;

	/**
	 * Die ID des zugeordneten Faches. Beispielsweise gehört der Kurs 'D-LK1' zum
	 * Fach 'D'.
	 */
	public long fach;

	/**
	 * Die ID der zugeordneten Kursart. Beispielsweise gehört der Kurs 'D-LK1' zur
	 * Kursart 'LK'.
	 */
	public long kursart;

	/**
	 * Definiert die Anzahl an Schienen, die der Kurs belegt. Üblich ist der Wert 1,
	 * in seltenen Fällen der Wert 2. Alle Kurse der selben Fachart (Fach + Kursart)
	 * sollten den gleichen Wert haben, andernfalls wird eine Warnung generiert.
	 */
	public int schienen;

	/**
	 * Eine String-Darstellung des Kurses, damit bei Warnungen oder Fehlern dem
	 * Benutzer diese angezeigt werden kann, beispielsweise 'D-GK1'.
	 */
	public @NotNull String representation = "";

}
