package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Schülergruppe-Schüler-Zuordnung.
 */
@TranspilerDTO
@Schema(description = "die Informationen zum Erstellen einer UV-Schülergruppe-Schüler-Zuordnung.")
public class UvSchuelergruppeSchuelerCreateRequest {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	public @NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.") Long idPlanungsabschnitt = 0L;

	/** Die ID der Schülergruppe. */
	@Schema(description = "die ID der Schülergruppe.")
	public @NotNull(message = "Die ID der Schülergruppe muss gesetzt werden.") Long idSchuelergruppe = 0L;

	/** Die ID des Schülers. */
	@Schema(description = "die ID des Schülers.")
	public @NotNull(message = "Die ID des Schülers muss gesetzt werden.") Long idSchueler = 0L;

}
