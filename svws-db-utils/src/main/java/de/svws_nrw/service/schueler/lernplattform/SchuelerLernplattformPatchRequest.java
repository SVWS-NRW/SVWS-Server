package de.svws_nrw.service.schueler.lernplattform;

import org.openapitools.jackson.nullable.JsonNullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Patch-Request-DTO für die Einwilligungen eines Schülers zu einer Lernplattform.
 * Alle Felder sind als {@link JsonNullable} deklariert, um zwischen "nicht gesetzt" (undefined)
 * und "explizit gesetzt" unterscheiden zu können. Nur gesetzte Felder werden auf die Entität angewendet.
 * Die IDs des Schülers und der Lernplattform bilden die ID der Zuordnung und sind nicht patchbar.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchuelerLernplattformPatchRequest {

	/** Die Abfrage der Einwilligung zu einer Lernplattform. */
	@Schema(description = "Die Abfrage der Einwilligung zu einer Lernplattform", example = "true")
	public JsonNullable<@NotNull Boolean> einwilligungAbgefragt = JsonNullable.undefined();

	/** Die Einwilligung zur Nutzung einer Lernplattform. */
	@Schema(description = "Die Einwilligung zur Nutzung einer Lernplattform.", example = "true")
	public JsonNullable<@NotNull Boolean> einwilligungNutzung = JsonNullable.undefined();

	/** Die Einwilligung zur Audiokonferenz einer Lernplattform. */
	@Schema(description = "Die Einwilligung zur Audiokonferenz einer Lernplattform.", example = "true")
	public JsonNullable<@NotNull Boolean> einwilligungAudiokonferenz = JsonNullable.undefined();

	/** Die Einwilligung zur Videokonferenz einer Lernplattform. */
	@Schema(description = "Die Einwilligung zur Videokonferenz einer Lernplattform.", example = "true")
	public JsonNullable<@NotNull Boolean> einwilligungVideokonferenz = JsonNullable.undefined();

}
