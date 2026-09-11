package de.svws_nrw.service.uv.klassen;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Klassen-Lehrer-Zuordnung.
 */
@Schema(description = "Patch-Daten für eine UV-Klassen-Lehrer-Zuordnung.")
public class UvKlassenLehrerPatchRequest {

	/** Die ID der Zuordnung. */
	@Schema(description = "die ID der Zuordnung", example = "4711")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Lehrers.")
	public JsonNullable<@NotNull(message = "Die ID des Lehrers darf nicht null sein.") Long> idLehrer = JsonNullable.undefined();

	/** Die Reihenfolge der Zuordnung. */
	@Schema(description = "die Reihenfolge der Zuordnung.")
	public JsonNullable<@NotNull(message = "Die Reihenfolge darf nicht null sein.") Integer> reihenfolge = JsonNullable.undefined();

}
