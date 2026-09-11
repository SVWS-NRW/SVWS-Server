package de.svws_nrw.service.uv.schueler;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Planungsabschnitt-Schüler-Zuordnung.
 */
@Schema(description = "die Informationen zum Erstellen einer UV-Planungsabschnitt-Schüler-Zuordnung.")
public class UvPlanungsabschnittSchuelerCreateRequest {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt;

	/** Die ID des Schülers. */
	@Schema(description = "die ID des Schülers.")
	@NotNull(message = "Die ID des Schülers muss gesetzt werden.")
	public Long idSchueler;

	/** Die ID des Jahrgangs. */
	@Schema(description = "die ID des Jahrgangs.")
	@NotNull(message = "Die ID des Jahrgangs muss gesetzt werden.")
	public Long idJahrgang;

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse.")
	public Long idKlasse;

}
