package de.svws_nrw.core.data.gost;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die Core-DTO für eine Kurs-Schüler-Zuordnung eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu einer Kurs-Schüler-Zuordnung eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostBlockungsergebnisKursSchuelerZuordnung {

	/** Die ID des Kurses */
	public long idKurs = -1;

	/** Die ID des Schülers */
	public long idSchueler = -1;

}
