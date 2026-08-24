package de.svws_nrw.service.schule.katalog.religion;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class ReligionPatchRequest {

	/** Die Bezeichnung bzw. der Name der Religion. */
	@Schema(description = "die Bezeichnung bzw. der Name der Religion", example = "röm.-kath.")
	public JsonNullable<@NotBlank @Size(max = 30) @NoLeadingOrTrailingWhitespaces String> bezeichnung = JsonNullable.undefined();

	/** Die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint. */
	@Schema(description = "die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint", example = "katholisch")
	public JsonNullable<@Size(max = 50) @NoLeadingOrTrailingWhitespaces String> bezeichnungZeugnis = JsonNullable.undefined();

	/** Die ID des Eintrages für die Statistik. */
	@Schema(description = "Die ID des Eintrages für die Statistik.", example = "1000")
	public JsonNullable<@NotNull Long> idReligion = JsonNullable.undefined();

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public JsonNullable<@NotNull Integer> sortierung = JsonNullable.undefined();

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

}
