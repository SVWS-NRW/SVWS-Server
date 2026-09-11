package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Lerngruppen-Schienen-Zuordnung.
 */
@TranspilerDTO
@Schema(description = "die Informationen zum Erstellen einer UV-Lerngruppen-Schienen-Zuordnung.")
public class UvLerngruppenSchieneCreateRequest {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt = 0L;

	/** Die ID der Lerngruppe. */
	@Schema(description = "die ID der Lerngruppe.")
	@NotNull(message = "Die ID der Lerngruppe muss gesetzt werden.")
	public Long idLerngruppe = 0L;

	/** Die ID der Schiene. */
	@Schema(description = "die ID der Schiene.")
	@NotNull(message = "Die ID der Schiene muss gesetzt werden.")
	public Long idSchiene = 0L;

}
