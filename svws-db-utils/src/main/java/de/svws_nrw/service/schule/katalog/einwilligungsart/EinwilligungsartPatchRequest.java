package de.svws_nrw.service.schule.katalog.einwilligungsart;

import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Der Request zum partiellen Aktualisieren einer Einwilligungsart im Katalog.
 * Die ID und der Personentyp der Einwilligungsart sind nicht patchbar.
 */
public class EinwilligungsartPatchRequest {

	/** Die Bezeichnung der Einwilligungsart */
	@Schema(description = "Die Bezeichnung der Einwilligungsart", example = "Verwendung Foto")
	public JsonNullable<@NotBlank @Size(max = 250) @NoLeadingOrTrailingWhitespaces String> bezeichnung = JsonNullable.undefined();

	/** Der Schlüssel der Einwilligungsart, ein leerer Schlüssel entfernt den Schlüssel */
	@Schema(description = "Der Schlüssel der Einwilligungsart", example = "FOTO")
	public JsonNullable<@Size(max = 20) @NoWhitespaces String> schluessel = JsonNullable.undefined();

	/** Eine ausführliche Beschreibung der Einwilligungsart */
	@Schema(description = "Eine ausführliche Beschreibung der Einwilligungsart", example = "Einwilligung zur Verwendung von Fotos")
	public JsonNullable<String> beschreibung = JsonNullable.undefined();

	/** Die Position in der Sortierreihenfolge der Katalog-Einträge */
	@Schema(description = "Die Position in der Sortierreihenfolge der Katalog-Einträge", example = "32000")
	public JsonNullable<@NotNull Integer> sortierung = JsonNullable.undefined();

	/** Gibt an, ob die Einwilligungsart in der Anwendung sichtbar ist */
	@Schema(description = "Gibt an, ob die Einwilligungsart in der Anwendung sichtbar ist", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

}
