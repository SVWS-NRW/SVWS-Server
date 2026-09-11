package de.svws_nrw.service.uv.schienen;

import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Schiene.
 */
@Schema(description = "Patch-Daten für eine UV-Schiene.")
public class UvSchienePatchRequest {

	/** Die ID der Schiene. */
	@Schema(description = "die ID der Schiene", example = "4711")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die Nummer der Schiene. */
	@Schema(description = "die Nummer der Schiene.")
	public JsonNullable<@NotNull(message = "Die Nummer der Schiene darf nicht null sein.") Integer> nummer = JsonNullable.undefined();

	/** Die Bezeichnung der Schiene. */
	@Schema(description = "die Bezeichnung der Schiene.")
	public JsonNullable<String> bezeichnung = JsonNullable.undefined();

	/** Die IDs der erlaubten Jahrgänge. */
	@Schema(description = "die IDs der erlaubten Jahrgänge.")
	public JsonNullable<
			@NotNull(message = "Die IDs der erlaubten Jahrgänge dürfen nicht null sein.") List<Long>> idsJahrgaengeErlaubt = JsonNullable.undefined();

}
