package de.nrw.schule.svws.core.data.kursblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die
 * Zuordnung eines Kurses zu seiner Schiene. Pro Kurs wird genau ein Objekt
 * dieser Klasse erzeugt. In seltenen Fällen, kann ein Kurs auch mehreren
 * Schienen zugeordnet sein, aus diesem Grund ist das Attribut
 * {@link KursblockungOutputKursZuSchiene#schienen} ein Integer-Array.
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "KursblockungOutputKursZuSchiene")
@Schema(name = "KursblockungOutputKursZuSchiene", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Zurdnung eines Kurses zu einer Schiene beim der Ausgabe des Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungOutputKursZuSchiene {

	/**
	 * Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei
	 * in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten
	 * aufsteigend ab 1.
	 */
	@Schema(required = true, description = "Die Revision der Kursblockungs-Ausgabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.", example = "1")
	public int enmRevision = -1;

	/**
	 * Die ID des Kurses.
	 */
	public long kurs;

	/**
	 * Die zugeordnete(n) Schiene(n). Die Werte sind 0-indiziert. Falls der Kurs in
	 * mehreren Schienen vertreten ist, dann hat das Array eine entsprechende Länge.
	 * Meistens ist die Länge des Arrays 1.
	 */
	public @NotNull int[] schienen = new int[0];

}
