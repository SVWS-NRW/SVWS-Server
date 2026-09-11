package de.svws_nrw.core.data.uv;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Schiene.
 */
@TranspilerDTO
@Schema(description = "die Informationen zum Erstellen einer UV-Schiene.")
public class UvSchieneCreateRequest {

	/** Die negative temporäre ID beim Sammelimport. */
	public @NotNull Long id = 0L;

	/** Die ID des Planungsabschnitts, in dem die Schiene gilt. */
	@Schema(description = "die ID des Planungsabschnitts, in dem die Schiene gilt.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt = 0L;

	/** Die Nummer der Schiene. */
	@Schema(description = "die Nummer der Schiene.")
	@NotNull(message = "Die Nummer muss gesetzt werden.")
	public Integer nummer = 0;

	/** Die Bezeichnung der Schiene. */
	@Schema(description = "die Bezeichnung der Schiene.")
	public String bezeichnung;

	/** Ein Array mit den IDs der erlaubten Jahrgänge. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "ein Array mit den IDs der erlaubten Jahrgänge."))
	public @NotNull List<Long> idsJahrgaengeErlaubt = new ArrayList<>();

}
