package de.svws_nrw.service.uv.lerngruppen;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Lerngruppen-Lehrer-Zuordnung.
 */
@Schema(description = "Patch-Daten für eine UV-Lerngruppen-Lehrer-Zuordnung.")
public class UvLerngruppenLehrerPatchRequest {

	/** Die ID der Zuordnung. */
	@Schema(description = "die ID der Zuordnung", example = "4711")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Lehrers.")
	public JsonNullable<@NotNull(message = "Die ID des Lehrers darf nicht null sein.") Long> idLehrer = JsonNullable.undefined();

	/** Die Reihenfolge. */
	@Schema(description = "die Reihenfolge.")
	public JsonNullable<@NotNull(message = "Die Reihenfolge darf nicht null sein.") Integer> reihenfolge = JsonNullable.undefined();

	/** Die Wochenstunden. */
	@Schema(description = "die Wochenstunden.")
	public JsonNullable<@NotNull(message = "Die Wochenstunden dürfen nicht null sein.") Double> wochenstunden = JsonNullable.undefined();

	/** Die angerechneten Wochenstunden. */
	@Schema(description = "die angerechneten Wochenstunden.")
	public JsonNullable<
			@NotNull(message = "Die angerechneten Wochenstunden dürfen nicht null sein.") Double> wochenstundenAngerechnet = JsonNullable.undefined();

}
