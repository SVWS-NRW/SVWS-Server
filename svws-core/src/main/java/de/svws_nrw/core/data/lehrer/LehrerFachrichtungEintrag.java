package de.svws_nrw.core.data.lehrer;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt eine Fachrichtung eines Lehrers mit der übergebenen ID.
 */
@XmlRootElement
@Schema(description = "Eine Fachrichtung eines Lehrers.")
@TranspilerDTO
public class LehrerFachrichtungEintrag {

	/** Die ID des Lehrers. */
	@Schema(description = "Die ID des Lehrers.", example = "4711")
	public long id;

	/** Die ID der Fachrichtung. */
	@Schema(description = "Die ID der Fachrichtung.", example = "4712")
	public long idFachrichtung;

	/** Die ID des Anerkennungsgrund für die Fachrichtung. */
	@Schema(description = "Die ID des Anerkennungsgrund für die Fachrichtung.", example = "4713")
	public Long idAnerkennungsgrund;

}
