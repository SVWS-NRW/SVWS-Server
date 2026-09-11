package de.svws_nrw.service.uv.unterrichte;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Unterricht-Einheit.
 */
@Schema(description = "die Informationen zum Erstellen einer UV-Unterricht-Einheit.")
public class UvUnterrichtCreateRequest {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt;

	/** Die ID der Lerngruppe. */
	@Schema(description = "die ID der Lerngruppe.")
	@NotNull(message = "Die ID der Lerngruppe muss gesetzt werden.")
	public Long idLerngruppe;

	/** Die ID des Zeitraster-Eintrags. */
	@Schema(description = "die ID des Zeitraster-Eintrags.")
	public Long idZeitrasterEintrag;

}
