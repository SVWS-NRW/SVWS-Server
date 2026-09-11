package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Lerngruppen-Lehrer-Zuordnung.
 */
@TranspilerDTO
@Schema(description = "die Informationen zum Erstellen einer UV-Lerngruppen-Lehrer-Zuordnung.")
public class UvLerngruppenLehrerCreateRequest {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt = 0L;

	/** Die ID der Lerngruppe. */
	@Schema(description = "die ID der Lerngruppe.")
	@NotNull(message = "Die ID der Lerngruppe muss gesetzt werden.")
	public Long idLerngruppe = 0L;

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Lehrers.")
	@NotNull(message = "Die ID des Lehrers muss gesetzt werden.")
	public Long idLehrer = 0L;

	/** Die Reihenfolge. */
	@Schema(description = "die Reihenfolge.")
	@NotNull(message = "Die Reihenfolge muss gesetzt werden.")
	public Integer reihenfolge = 0;

	/** Die Wochenstunden. */
	@Schema(description = "die Wochenstunden.")
	@NotNull(message = "Die Wochenstunden müssen gesetzt werden.")
	public Double wochenstunden = 0.0;

	/** Die angerechneten Wochenstunden. */
	@Schema(description = "die angerechneten Wochenstunden.")
	@NotNull(message = "Die angerechneten Wochenstunden müssen gesetzt werden.")
	public Double wochenstundenAngerechnet = 0.0;

}
