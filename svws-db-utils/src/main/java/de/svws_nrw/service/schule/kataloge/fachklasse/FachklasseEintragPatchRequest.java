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

	/** Die Bezeichnung (weibliche Form) */
	@Schema(description = "Die Bezeichnung (weibliche Form)", example = "Anlagenmechanikerin")
	public JsonNullable<@Size(max = 100) @NotBlank @NoLeadingOrTrailingWhitespaces String> bezeichnungWeiblich = JsonNullable.undefined();

	/** Das Kürzel */
	@Schema(description = "Das Kürzel", example = "AM")
	public JsonNullable<@Size(max = 100) @NoWhitespaces String> kuerzel = JsonNullable.undefined();

	/** Die ID der Fachklasse (CoreType) */
	@Schema(description = "Die ID der Fachklasse (CoreType)", example = "7000")
	public JsonNullable<@NotNull Long> idFachklasse = JsonNullable.undefined();

	/** Die ID des DQR-Niveaus (CoreType) */
	@Schema(description = "Die ID des DQR-Niveaus (CoreType)", example = "3")
	public JsonNullable<Integer> idDqrNiveau = JsonNullable.undefined();

	/** Die 1. Berufsebene */
	@Schema(description = "Die 1. Berufsebene", example = "TN")
	public JsonNullable<@Size(max = 255) @NoLeadingOrTrailingWhitespaces String> berufsebene1 = JsonNullable.undefined();

	/** Die 2. Berufsebene */
	@Schema(description = "Die 2. Berufsebene", example = "TN")
	public JsonNullable<@Size(max = 255) @NoLeadingOrTrailingWhitespaces String> berufsebene2 = JsonNullable.undefined();

	/** Die 3. Berufsebene */
	@Schema(description = "Die 3. Berufsebene", example = "TN")
	public JsonNullable<@Size(max = 255) @NoLeadingOrTrailingWhitespaces String> berufsebene3 = JsonNullable.undefined();

	/** Die Sichtbarkeit */
	@Schema(description = "Die Sichtbarkeit", example = "true")
	public JsonNullable<Boolean> istSichtbar = JsonNullable.undefined();

	/** Die Sortierung */
	@Schema(description = "Die Sortierung", example = "32000")
	public JsonNullable<Integer> sortierung = JsonNullable.undefined();

}
