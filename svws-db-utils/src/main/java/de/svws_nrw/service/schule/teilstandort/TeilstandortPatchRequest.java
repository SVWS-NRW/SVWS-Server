package de.svws_nrw.service.schule.teilstandort;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class TeilstandortPatchRequest {

	/** Die Postleitzahl. */
	@Schema(description = "die Postleitzahl", example = "53840")
	public JsonNullable<@Size(max = 10) String> plz = JsonNullable.undefined();

	/** Der Ort des Teilstandortes */
	@Schema(description = "Der Ort des Teilstandortes", example = "Düsseldorf")
	public JsonNullable<@Size(max = 50) String> ort = JsonNullable.undefined();

	/** Der Straßenname des Teilstandortes */
	@Schema(description = "Der Straßenname des Teilstandortes", example = "Muster Str.")
	public JsonNullable<@Size(max = 55) String> strassenname = JsonNullable.undefined();

	/** Die Hausnummer des Teilstandortes */
	@Schema(description = "Die Hausnummer des Teilstandortes", example = "4")
	public JsonNullable<@Size(max = 10) String> hausNr = JsonNullable.undefined();

	/** Der Hausnummernzusatz des Teilstandortes */
	@Schema(description = "Der Hausnummernzusatz des Teilstandortes", example = "a-c")
	public JsonNullable<@Size(max = 30) String> hausNrZusatz = JsonNullable.undefined();

	/** Eine Bemerkung zum Teilstandort */
	@Schema(description = "Eine Bemerkung zum Teilstandort", example = "Hauptgebäude")
	public JsonNullable<@Size(max = 50) String> bemerkung = JsonNullable.undefined();

	/** Das Kürzel des Teilstandortes */
	@Schema(description = "Das Kürzel des Teilstandortes", example = "Hauptst.")
	public JsonNullable<@Size(max = 30) String> kuerzel = JsonNullable.undefined();
}
