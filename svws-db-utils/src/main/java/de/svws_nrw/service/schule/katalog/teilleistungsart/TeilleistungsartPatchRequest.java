package de.svws_nrw.service.schule.katalog.teilleistungsart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

@Schema(description = "Anrechnungsstunden bei Lehrerabschnittsdaten.")
public class TeilleistungsartPatchRequest {

	/**
	 * Json Nullable Repräsentation der Bezeichnung
	 */
	@Schema(description = "Die Bezeichnung.", example = "Sek1-Somi1")
	public JsonNullable<@NotNull @Size(min = 1, max = 50) String> bezeichnung = JsonNullable.undefined();

	/**
	 * Json Nullable Repräsentation der Sortierung
	 */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Teilleistungsart an", example = "1")
	public JsonNullable<@Max(32000) Integer> sortierung = JsonNullable.undefined();

	/**
	 * Json Nullable Repräsentation der Sichtbarkeit
	 */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Teilleistungsart an", example = "1")
	public JsonNullable<Boolean> istSichtbar = JsonNullable.undefined();

}
