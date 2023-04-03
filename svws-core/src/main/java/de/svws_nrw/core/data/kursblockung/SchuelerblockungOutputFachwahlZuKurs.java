package de.svws_nrw.core.data.kursblockung;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse ordnet der Fachwahl eines Schülers einen Kurs zu.
 *
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "SchuelerblockungOutputFachwahlZuKurs")
@Schema(name = "SchuelerblockungOutputFachwahlZuKurs", description = "Diese Klasse ordnet der Fachwahl eines Schülers einen Kurs zu.")
@TranspilerDTO
public class SchuelerblockungOutputFachwahlZuKurs {

	/** Die ID des Faches (des Schülers). */
	public long fachID;

	/** Die ID der Kursart (des Schülers). */
	public int kursartID;

	/** Die ID des zugeordneten Kurses zur Fachwahl (des Schülers). Ein Wert von {@code -1} bedeutet, dass das Fach
	 * nicht zugeordnet werden konnte, was als <b>Nicht-Wahl</b> bezeichnet wird. */
	public long kursID;

}
