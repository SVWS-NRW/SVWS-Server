package de.svws_nrw.service.schule.katalog.lernplattform;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Der Request zum Erstellen einer neuen Lernplattform im Katalog.
 */
public class LernplattformCreateRequest {

	/** Die Bezeichnung der Lernplattform */
	@Schema(description = "Die Bezeichnung der Lernplattform", example = "IServ")
	@NotBlank
	@Size(max = 255)
	@NoLeadingOrTrailingWhitespaces
	public String bezeichnung;
}
