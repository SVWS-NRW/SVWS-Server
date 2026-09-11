package de.svws_nrw.service.uv.stundentafeln;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Stundentafel.
 */
@Schema(description = "Patch-Daten für eine UV-Stundentafel.")
public class UvStundentafelPatchRequest {

	/** Die ID der UV-Stundentafel. */
	@Schema(description = "die ID der UV-Stundentafel", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die Bezeichnung der Stundentafel. */
	@Schema(description = "Die Bezeichnung der Stundentafel.")
	public JsonNullable<@NotNull(message = "Die Bezeichnung darf nicht null sein.") String> bezeichnung = JsonNullable.undefined();

	/** Das Datum, ab dem die Stundentafel gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem die Stundentafel gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem die Stundentafel gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem die Stundentafel gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigBis = JsonNullable.undefined();

	/** Eine optionale Beschreibung. */
	@Schema(description = "Eine optionale Beschreibung.")
	public JsonNullable<String> beschreibung = JsonNullable.undefined();

}
