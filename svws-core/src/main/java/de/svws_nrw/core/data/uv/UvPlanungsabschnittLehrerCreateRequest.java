package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Planungsabschnitt-Lehrer-Zuordnung.
 */
@TranspilerDTO
@Schema(description = "die Informationen zum Erstellen einer UV-Planungsabschnitt-Lehrer-Zuordnung.")
public class UvPlanungsabschnittLehrerCreateRequest {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt = 0L;

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Lehrers.")
	@NotNull(message = "Die ID des Lehrers muss gesetzt werden.")
	public Long idLehrer = 0L;

}
