package de.svws_nrw.service.uv.faecher;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für ein UV-Fach.
 */
@Schema(description = "Patch-Daten für ein UV-Fach.")
public class UvFachPatchRequest {

	/** Die ID des UV-Fachs. */
	@Schema(description = "die ID des UV-Fachs", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Das Datum, ab dem das Fach gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem das Fach gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem das Fach gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem das Fach gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigBis = JsonNullable.undefined();

}
