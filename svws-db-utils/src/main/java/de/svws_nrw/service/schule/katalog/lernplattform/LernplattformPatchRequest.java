package de.svws_nrw.service.schule.katalog.lernplattform;

import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Der Request zum partiellen Aktualisieren einer Lernplattform im Katalog.
 * Die ID der Lernplattform ist nicht patchbar.
 */
public class LernplattformPatchRequest {

	/** Die Bezeichnung der Lernplattform */
	@Schema(description = "Die Bezeichnung der Lernplattform", example = "IServ")
	@NotBlank
	@Size(max = 255)
	@NoLeadingOrTrailingWhitespaces
	public JsonNullable<String> bezeichnung = JsonNullable.undefined();
}
