package de.svws_nrw.service.schule.katalog.ort;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class OrtPatchRequest {

	/** Die Postleitzahl. */
	@Schema(description = "die Postleitzahl", example = "53840")
	public JsonNullable<@NotBlank @Size(max = 10) @NoWhitespaces String> plz = JsonNullable.undefined();

	/** Der Name des Ortes. */
	@Schema(description = "der Name des Ortes", example = "Troisdorf")
	public JsonNullable<@NotBlank @Size(max = 50) @NoLeadingOrTrailingWhitespaces String> ortsname = JsonNullable.undefined();

	/** Der Name des Kreises. */
	@Schema(description = "der Name des Kreises", example = "RSK")
	public JsonNullable<@Size(max = 30) String> kreis = JsonNullable.undefined();

	/** Die ID des Bundeslandes. (CoreType: Laender) */
	@Schema(description = "Die ID des Bundeslandes. (CoreType: Laender)", example = "1090")
	public JsonNullable<Long> idBundesland = JsonNullable.undefined();

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public JsonNullable<@NotNull @Max(Integer.MAX_VALUE) Integer> sortierung = JsonNullable.undefined();

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istAenderbar = JsonNullable.undefined();

}
