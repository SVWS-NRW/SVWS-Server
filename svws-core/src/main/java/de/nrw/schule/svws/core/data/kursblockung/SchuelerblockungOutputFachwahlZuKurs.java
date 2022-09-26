package de.nrw.schule.svws.core.data.kursblockung;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/** Diese Klasse ordnet der Fachwahl eines Schülers einen Kurs zu.
 * 
 * @author Benjamin A. Bartsch */
@XmlRootElement(name = "SchuelerblockungOutputFachwahlZuKurs")
@Schema(name = "SchuelerblockungOutputFachwahlZuKurs", description = "Diese Klasse ordnet der Fachwahl eines Schülers einen Kurs zu.")
@TranspilerDTO
public class SchuelerblockungOutputFachwahlZuKurs {

	/** Die ID der Fachwahl (des Schülers). */
	public long fachwahl;

	/** Die ID des zugeordneten Kurses zur Fachwahl (des Schülers). Ein Wert von {@code -1} bedeutet, dass das Fach
	 * nicht zugeordnet werden konnte, was als <b>Nicht-Wahl</b> bezeichnet wird. */
	public long kurs;

}
