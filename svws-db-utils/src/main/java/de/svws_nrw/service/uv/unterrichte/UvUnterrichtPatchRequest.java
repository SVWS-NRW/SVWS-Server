package de.svws_nrw.service.uv.unterrichte;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Unterricht-Einheit.
 */
@Schema(description = "Patch-Daten für eine UV-Unterricht-Einheit.")
public class UvUnterrichtPatchRequest {

	/** Die ID der Unterricht-Einheit. */
	@Schema(description = "die ID der Unterricht-Einheit", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die ID der Lerngruppe. */
	@Schema(description = "die ID der Lerngruppe.")
	public JsonNullable<@NotNull(message = "Die ID der Lerngruppe darf nicht null sein.") Long> idLerngruppe = JsonNullable.undefined();

	/** Die ID des Zeitraster-Eintrags. */
	@Schema(description = "die ID des Zeitraster-Eintrags.")
	public JsonNullable<Long> idZeitrasterEintrag = JsonNullable.undefined();

}
