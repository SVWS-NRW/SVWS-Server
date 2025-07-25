package de.svws_nrw.asd.data.kurse;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Informationen zu einer zusätzlichen Lehrkraft in einem Kurs.
 */
@XmlRootElement
@Schema(description = "Die Daten eines KursLehrers.")
@TranspilerDTO
public class KursLehrer {

	/** Die ID des Kurslehrers. */
	@Schema(description = "Die ID des Kurslehrers", example = "42", nullable = true)
	public long idLehrer;

	/** Die ID des Kurses */
	@Schema(description = "Die ID des Kurses", example = "1", nullable = true)
	public long idKurs;

	/** Die Wochenstunden des Kurslehrers in dem Kurs. */
	@Schema(description = "die Wochenstunden des Kurslehrers in dem Kurs", example = "3")
	public double wochenstundenLehrer = -1;

}
