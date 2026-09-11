package de.svws_nrw.service.uv.raeume;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für einen UV-Raum.
 */
@Schema(description = "Patch-Daten für einen UV-Raum.")
public class UvRaumPatchRequest {

	/** Die ID des UV-Raums. */
	@Schema(description = "die ID des UV-Raums", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Das Kürzel des Raums. */
	@Schema(description = "Das Kürzel des Raums.")
	public JsonNullable<
			@NotNull(message = "Das Kürzel des Raums muss gesetzt werden.")
			@Size(max = 20, message = "Das Kürzel des Raums darf maximal 20 Zeichen lang sein.") String> kuerzel = JsonNullable.undefined();

	/** Die Beschreibung des Raums. */
	@Schema(description = "Die Beschreibung des Raums.")
	public JsonNullable<String> beschreibung = JsonNullable.undefined();

	/** Die Größe des Raums an Arbeitsplätzen für Schüler. */
	@Schema(description = "Die Größe des Raums an Arbeitsplätzen für Schüler.")
	public JsonNullable<@NotNull(message = "Die Größe des Raums muss gesetzt werden.") Integer> groesse = JsonNullable.undefined();

	/** Die ID der Raumgruppe, falls der Raum zu einer solchen gehört. */
	@Schema(description = "Die ID der Raumgruppe, falls der Raum zu einer solchen gehört.")
	public JsonNullable<Long> idRaumgruppe = JsonNullable.undefined();

	/** Das Datum, ab dem der Raum gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem der Raum gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<@NotNull(message = "Das Datum 'gueltigVon' muss gesetzt werden.") @ValidDateFormat String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem der Raum gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem der Raum gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<@ValidDateFormat String> gueltigBis = JsonNullable.undefined();

}
