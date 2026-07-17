package de.svws_nrw.service.schule.kataloge.fachklasse;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class FachklasseEintragPatchRequest {

	/** Die Bezeichnung */
	@Schema(description = "Die Bezeichnung", example = "Anlagenmechaniker/-in")
	public JsonNullable<@Size(max = 100) @NotBlank @NoLeadingOrTrailingWhitespaces String> bezeichnung = JsonNullable.undefined();

	/** Das Kürzel */
	@Schema(description = "Das Kürzel", example = "AM")
	public JsonNullable<@Size(max = 100) @NoWhitespaces String> kuerzel = JsonNullable.undefined();

	/** Die ID der Fachklasse (CoreType) */
	@Schema(description = "Die ID der Fachklasse (CoreType)", example = "7000")
	public JsonNullable<@NotNull Long> idFachklasse = JsonNullable.undefined();

	/** Die Sichtbarkeit */
	@Schema(description = "Die Sichtbarkeit", example = "true")
	public JsonNullable<Boolean> istSichtbar = JsonNullable.undefined();

	/** Die Sortierung */
	@Schema(description = "Die Sortierung", example = "32000")
	public JsonNullable<Integer> sortierung = JsonNullable.undefined();

}
