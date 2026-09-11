package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt eine Schülergruppe innerhalb eines Planungsabschnitts im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt eine Schülergruppe innerhalb eines Planungsabschnitts im UV-Export.")
@TranspilerDTO
public class UVv1SchuelergruppeExport {

	/** Die UV-ID der Schülergruppe. */
	@Schema(description = "die UV-ID der Schülergruppe", example = "815")
	public long uvId = -1;

	/** Die Bezeichnung der Schülergruppe. */
	@Schema(description = "die Bezeichnung der Schülergruppe", example = "Religion 5ab")
	public @NotNull String bezeichnung = "";

	/** Ein Array mit den IDs der Schüler der Gruppe. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "ein Array mit den IDs der Schüler der Gruppe"))
	public @NotNull List<Long> schuelerIds = new ArrayList<>();

}
