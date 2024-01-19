package de.svws_nrw.core.data.gost;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die Core-DTO für eine Kurs-Schienen-Zuordnung eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu einer Kurs-Schienen-Zuordnung eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostBlockungsergebnisKursSchienenZuordnung {

	/** Die ID des Kurses */
	public long idKurs = -1;

	/** Die ID der Schiene */
	public long idSchiene = -1;

}
