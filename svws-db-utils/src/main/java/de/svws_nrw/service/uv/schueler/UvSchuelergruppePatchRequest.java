package de.svws_nrw.service.uv.schueler;

import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Schülergruppe.
 */
@Schema(description = "Patch-Daten für eine UV-Schülergruppe.")
public class UvSchuelergruppePatchRequest {

	/** Die ID der Schülergruppe. */
	@Schema(description = "die ID der Schülergruppe", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die Bezeichnung der Schülergruppe. */
	@Schema(description = "die Bezeichnung der Schülergruppe.")
	public JsonNullable<
			@NotNull(message = "Die Bezeichnung der Schülergruppe muss gesetzt werden.")
			@Size(max = 50, message = "Die Bezeichnung der Schülergruppe darf maximal 50 Zeichen lang sein.") String> bezeichnung = JsonNullable.undefined();

	/** Die IDs der erlaubten Jahrgänge. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "die IDs der erlaubten Jahrgänge."))
	public JsonNullable<
			@NotNull(message = "Die IDs der erlaubten Jahrgänge müssen gesetzt werden.") List<Long>> idsJahrgaengeErlaubt = JsonNullable.undefined();

	/** Die IDs der erlaubten Gruppen. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "die IDs der erlaubten Gruppen."))
	public JsonNullable<
			@NotNull(message = "Die IDs der erlaubten Gruppen müssen gesetzt werden.") List<Long>> idsGruppenErlaubt = JsonNullable.undefined();

}
