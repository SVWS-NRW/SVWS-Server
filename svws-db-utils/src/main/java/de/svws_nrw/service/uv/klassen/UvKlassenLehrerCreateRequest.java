package de.svws_nrw.service.uv.klassen;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Klassen-Lehrer-Zuordnung.
 */
@Schema(description = "die Informationen zum Erstellen einer UV-Klassen-Lehrer-Zuordnung.")
public class UvKlassenLehrerCreateRequest {

	/** Die ID des Planungsabschnitts, in dem die Zuordnung gilt. */
	@Schema(description = "die ID des Planungsabschnitts, in dem die Zuordnung gilt.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt;

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse.")
	@NotNull(message = "Die ID der Klasse muss gesetzt werden.")
	public Long idKlasse;

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Lehrers.")
	@NotNull(message = "Die ID des Lehrers muss gesetzt werden.")
	public Long idLehrer;

	/** Die Reihenfolge der Zuordnung. */
	@Schema(description = "die Reihenfolge der Zuordnung.")
	@NotNull(message = "Die Reihenfolge muss gesetzt werden.")
	public Integer reihenfolge;

}
