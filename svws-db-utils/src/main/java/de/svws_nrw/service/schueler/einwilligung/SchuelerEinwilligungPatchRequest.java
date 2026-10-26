package de.svws_nrw.service.schueler.einwilligung;

import org.openapitools.jackson.nullable.JsonNullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Patch-Request-DTO für die Einwilligung eines Schülers zu einer Einwilligungsart.
 * Alle Felder sind als {@link JsonNullable} deklariert, um zwischen "nicht gesetzt" (undefined)
 * und "explizit gesetzt" unterscheiden zu können. Nur gesetzte Felder werden auf die Entität angewendet.
 * Die IDs des Schülers und der Einwilligungsart bilden die ID der Einwilligung und sind nicht patchbar.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchuelerEinwilligungPatchRequest {

	/** Der Status der Einwilligung (erteilt/nicht erteilt). */
	@Schema(description = "der Status der Einwilligung (erteilt oder nicht erteilt)", example = "true")
	public JsonNullable<@NotNull Boolean> status = JsonNullable.undefined();

	/** Der Status der Abfrage der Einwilligung (abgefragt/nicht abgefragt). */
	@Schema(description = "der Status der Abfrage der Einwilligung (abgefragt/nicht abgefragt)", example = "true")
	public JsonNullable<@NotNull Boolean> abgefragt = JsonNullable.undefined();

}
