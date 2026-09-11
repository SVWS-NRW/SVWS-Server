package de.svws_nrw.service.uv.unterrichte;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Unterricht-Raum-Zuordnung.
 */
@Schema(description = "die Informationen zum Erstellen einer UV-Unterricht-Raum-Zuordnung.")
public class UvUnterrichtRaumCreateRequest {

	/** Die ID des Unterrichts. */
	@Schema(description = "die ID des Unterrichts.")
	@NotNull(message = "Die ID des Unterrichts muss gesetzt werden.")
	public Long idUnterricht;

	/** Die ID des Raums. */
	@Schema(description = "die ID des Raums.")
	@NotNull(message = "Die ID des Raums muss gesetzt werden.")
	public Long idRaum;

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt;

}
