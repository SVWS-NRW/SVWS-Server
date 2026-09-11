package de.svws_nrw.service.uv.zeitraster;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für ein UV-Zeitraster.
 */
@Schema(description = "Patch-Daten für ein UV-Zeitraster.")
public class UvZeitrasterPatchRequest {

	/** Die ID des UV-Zeitrasters. */
	@Schema(description = "die ID des UV-Zeitrasters", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die Bezeichnung des Zeitrasters. */
	@Schema(description = "Die Bezeichnung des Zeitrasters.")
	public JsonNullable<@NotNull(message = "Die Bezeichnung darf nicht null sein.") String> bezeichnung = JsonNullable.undefined();

	/** Das Datum, ab dem das Zeitraster gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem das Zeitraster gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem das Zeitraster gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem das Zeitraster gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigBis = JsonNullable.undefined();

}
