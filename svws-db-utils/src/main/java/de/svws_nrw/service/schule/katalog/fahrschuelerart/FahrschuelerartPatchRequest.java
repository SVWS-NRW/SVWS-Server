package de.svws_nrw.service.schule.katalog.fahrschuelerart;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Der Request zum partiellen Aktualisieren einer Fahrschülerart im Katalog.
 * Die ID der Fahrschülerart ist nicht patchbar.
 */
public class FahrschuelerartPatchRequest {

	/** Die Bezeichnung der Fahrschülerart */
	@Schema(description = "Die Bezeichnung der Fahrschülerart", example = "Bus")
	public JsonNullable<@NotBlank @Size(max = 30) @NoLeadingOrTrailingWhitespaces String> bezeichnung = JsonNullable.undefined();

	/** Die Position in der Sortierreihenfolge der Katalog-Einträge */
	@Schema(description = "Die Position in der Sortierreihenfolge der Katalog-Einträge", example = "32000")
	public JsonNullable<@NotNull Integer> sortierung = JsonNullable.undefined();

	/** Gibt an, ob die Fahrschülerart in der Anwendung sichtbar ist */
	@Schema(description = "Gibt an, ob die Fahrschülerart in der Anwendung sichtbar ist", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

	/** Gibt an, ob die Fahrschülerart in der Anwendung änderbar ist */
	@Schema(description = "Gibt an, ob die Fahrschülerart in der Anwendung änderbar ist", example = "true")
	public JsonNullable<@NotNull Boolean> istAenderbar = JsonNullable.undefined();
}
