package de.svws_nrw.core.data.lehrer;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt ein Lehramt eines Lehrers mit der übergebenen ID.
 */
@XmlRootElement
@Schema(description = "Ein Lehramt eines Lehrers.")
@TranspilerDTO
public class LehrerLehramtEintrag {

	/** Die ID des Lehrers. */
	@Schema(description = "Die ID des Lehrers.", example = "4711")
	public long id;

	/** Die ID des Lehramtes. */
	@Schema(description = "Die ID des Lehramtes.", example = "4712")
	public long idLehramt;

	/** Die ID des Anerkennungsgrund für das Lehramt. */
	@Schema(description = "Die ID des Anerkennungsgrund für das Lehramt.", example = "4713")
	public Long idAnerkennungsgrund;

}
