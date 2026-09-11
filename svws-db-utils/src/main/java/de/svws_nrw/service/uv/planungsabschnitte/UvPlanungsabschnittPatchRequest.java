package de.svws_nrw.service.uv.planungsabschnitte;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für einen UV-Planungsabschnitt.
 */
@Schema(description = "Patch-Daten für einen UV-Planungsabschnitt.")
public class UvPlanungsabschnittPatchRequest {

	/** Die ID des UV-Planungsabschnitts. */
	@Schema(description = "die ID des UV-Planungsabschnitts", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Gibt an, ob der Planungsabschnitt aktiv ist. */
	@Schema(description = "Gibt an, ob der Planungsabschnitt aktiv ist.")
	public JsonNullable<@NotNull(message = "Das Attribut 'aktiv' darf nicht null sein.") Boolean> aktiv = JsonNullable.undefined();

	/** Das Datum, ab dem der Planungsabschnitt gültig ist. */
	@Schema(description = "Das Datum, ab dem der Planungsabschnitt gültig ist.")
	public JsonNullable<String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem der Planungsabschnitt gültig ist. */
	@Schema(description = "Das Datum, bis zu dem der Planungsabschnitt gültig ist.")
	public JsonNullable<String> gueltigBis = JsonNullable.undefined();

	/** Optionale Beschreibung zum Planungsabschnitt. */
	@Schema(description = "Optionale Beschreibung zum Planungsabschnitt.")
	public JsonNullable<String> beschreibung = JsonNullable.undefined();

}
