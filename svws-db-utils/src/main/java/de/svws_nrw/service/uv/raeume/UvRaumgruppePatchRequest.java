package de.svws_nrw.service.uv.raeume;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Raumgruppe.
 */
@Schema(description = "Patch-Daten für eine UV-Raumgruppe.")
public class UvRaumgruppePatchRequest {

	/** Die ID der UV-Raumgruppe. */
	@Schema(description = "die ID der UV-Raumgruppe", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die Bezeichnung der Raumgruppe. */
	@Schema(description = "Die Bezeichnung der Raumgruppe.")
	public JsonNullable<
			@NotNull(message = "Die Bezeichnung der Raumgruppe muss gesetzt werden.")
			@Size(max = 20, message = "Die Bezeichnung der Raumgruppe darf maximal 20 Zeichen lang sein.") String> bezeichnung = JsonNullable.undefined();

	/** Das Datum, ab dem die Raumgruppe gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem die Raumgruppe gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<@NotNull(message = "Das Datum 'gueltigVon' muss gesetzt werden.") @ValidDateFormat String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem die Raumgruppe gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem die Raumgruppe gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<@ValidDateFormat String> gueltigBis = JsonNullable.undefined();

	/** Eine Beschreibung der Raumgruppe. */
	@Schema(description = "Eine Beschreibung der Raumgruppe.")
	public JsonNullable<String> beschreibung = JsonNullable.undefined();

}
