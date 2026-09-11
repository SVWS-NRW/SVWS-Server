package de.svws_nrw.service.uv.zeitraster;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Ein Request-Objekt zum Patchen einer Zeitraster-Zuordnung zu einem Planungsabschnitt.
 */
public class UvPlanungsabschnittZeitrasterPatchRequest {

	/** Die ID des Planungsabschnitts */
	@NotNull(message = "Die ID des Planungsabschnitts des zu patchenden Objekts muss vorhanden sein.")
	public long idPlanungsabschnitt;

	/** Die ID des Zeitrasters */
	@NotNull(message = "Die ID des Zeitrasters des zu patchenden Objekts muss vorhanden sein.")
	public long idZeitraster;

	/** Die IDs der zugeordneten Jahrgänge */
	@JsonProperty("idsJahrgaenge")
	@Schema(description = "die IDs der zugeordneten Jahrgänge")
	public JsonNullable<
			@NotNull(message = "Die IDs der zugeordneten Jahrgänge dürfen nicht null sein.") List<Long>> idsJahrgaenge = JsonNullable.undefined();

}
