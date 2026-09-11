package de.svws_nrw.service.uv.schueler;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Planungsabschnitt-Schüler-Zuordnung.
 */
@Schema(description = "Patch-Daten für eine UV-Planungsabschnitt-Schüler-Zuordnung.")
public class UvPlanungsabschnittSchuelerPatchRequest {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts", example = "42")
	@NotNull(message = "Die ID des Planungsabschnitts des zu patchenden Objekts muss vorhanden sein.")
	public Long idPlanungsabschnitt;

	/** Die ID des Schülers. */
	@Schema(description = "die ID des Schülers", example = "4711")
	@NotNull(message = "Die ID des Schülers des zu patchenden Objekts muss vorhanden sein.")
	public Long idSchueler;

	/** Die ID des Jahrgangs. */
	@Schema(description = "die ID des Jahrgangs.")
	public JsonNullable<@NotNull(message = "Die ID des Jahrgangs darf nicht null sein.") Long> idJahrgang = JsonNullable.undefined();

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse.")
	public JsonNullable<Long> idKlasse = JsonNullable.undefined();

}
