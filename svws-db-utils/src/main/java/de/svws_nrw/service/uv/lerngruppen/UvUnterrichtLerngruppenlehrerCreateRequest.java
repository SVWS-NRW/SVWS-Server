package de.svws_nrw.service.uv.lerngruppen;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Unterricht-Lerngruppenlehrer-Zuordnung.
 */
@Schema(description = "die Informationen zum Erstellen einer UV-Unterricht-Lerngruppenlehrer-Zuordnung.")
public class UvUnterrichtLerngruppenlehrerCreateRequest {

	/** Die ID des Unterrichts. */
	@Schema(description = "die ID des Unterrichts.")
	@NotNull(message = "Die ID des Unterrichts muss gesetzt werden.")
	public Long idUnterricht;

	/** Die ID des Lerngruppenlehrers. */
	@Schema(description = "die ID des Lerngruppenlehrers.")
	@NotNull(message = "Die ID des Lerngruppenlehrers muss gesetzt werden.")
	public Long idLerngruppenLehrer;

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt;

}
