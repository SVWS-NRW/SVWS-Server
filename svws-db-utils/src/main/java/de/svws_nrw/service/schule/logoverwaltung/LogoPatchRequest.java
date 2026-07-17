package de.svws_nrw.service.schule.logoverwaltung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.openapitools.jackson.nullable.JsonNullable;

public class LogoPatchRequest {

	/** Das Logo als Bild im Base64-Format. */
	@Schema(description = "das Logo als Bild im Base64-Format")
	public JsonNullable<@NotBlank String> logoBase64 = JsonNullable.undefined();

}
