package de.svws_nrw.service.uv.zeitraster;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Ein Request-Objekt zum Erstellen einer neuen Zeitraster-Zuordnung zu einem Planungsabschnitt.
 */
public class UvPlanungsabschnittZeitrasterCreateRequest {

	/** Die ID des Planungsabschnitts */
	@Schema(description = "die ID des Planungsabschnitts", example = "42")
	public long idPlanungsabschnitt;

	/** Die ID des Zeitrasters */
	@Schema(description = "die ID des Zeitrasters", example = "4711")
	public long idZeitraster;

	/** Ein Array mit den IDs der zugeordneten Jahrgänge. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "ein Array mit den IDs der zugeordneten Jahrgänge."))
	public List<Long> idsJahrgaenge = new ArrayList<>();

}
