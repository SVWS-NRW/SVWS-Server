package de.svws_nrw.core.data.uv;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Schülergruppe.
 */
@TranspilerDTO
@Schema(description = "die Informationen zum Erstellen einer UV-Schülergruppe.")
public class UvSchuelergruppeCreateRequest {

	/** Die negative temporäre ID beim Sammelimport. */
	public @NotNull Long id = 0L;

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt = 0L;

	/** Die Bezeichnung der Schülergruppe. */
	@Schema(description = "die Bezeichnung der Schülergruppe.")
	@NotNull(message = "Die Bezeichnung der Schülergruppe muss gesetzt werden.")
	@Size(max = 50, message = "Die Bezeichnung der Schülergruppe darf maximal 50 Zeichen lang sein.")
	public String bezeichnung = "";

	/** Die IDs der erlaubten Jahrgänge. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "die IDs der erlaubten Jahrgänge."))
	public @NotNull List<Long> idsJahrgaengeErlaubt = new ArrayList<>();

	/** Die IDs der erlaubten Gruppen. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "die IDs der erlaubten Gruppen."))
	public @NotNull List<Long> idsGruppenErlaubt = new ArrayList<>();

}
