package de.nrw.schule.svws.core.data.kursblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Zuordnung der Fachwahl (eines Schülers)
 * {@link KursblockungOutputFachwahlZuKurs#fachwahl} zu einem Kurs {@link KursblockungOutputFachwahlZuKurs#kurs}. Pro
 * Fachwahl wird ein Objekt dieser Klasse erzeugt. Anhand der Fachwahl-ID kann die GUI die Daten
 * {@link KursblockungInputFachwahl#schueler}, {@link KursblockungInputFachwahl#fach} und
 * {@link KursblockungInputFachwahl#kursart} rekonstruieren. Im Falle einer <b>Nicht-Wahl</b> hat das Attribut
 * {@link KursblockungOutputFachwahlZuKurs#kurs} den Long-Wert {@code -1}.
 * 
 * @author Benjamin A. Bartsch */
@XmlRootElement(name = "KursblockungOutputFachwahlZuKurs")
@Schema(name = "KursblockungOutputFachwahlZuKurs", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Zurdnung einer Fachwahl eines Schülers zu einem Kurs bei der Ausgabe des Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungOutputFachwahlZuKurs {

	/** Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1
	 * für Entwickler-Revisionen und ansonsten aufsteigend ab 1. */
	@Schema(required = true, description = "Die Revision der Kursblockungs-Ausgabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.", example = "1")
	public int enmRevision = -1;

	/** Die ID der Fachwahl (des Schülers). */
	public long fachwahl;

	/** Die ID des zugeordneten Kurses zur Fachwahl (des Schülers). Ein Wert von {@code -1} bedeutet, dass das Fach
	 * nicht zugeordnet werden konnte, was als <b>Nicht-Wahl</b> bezeichnet wird. */
	public long kurs;

}
