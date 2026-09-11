package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt die Zuordnung eines Zeitrasters zu einem Planungsabschnitt.
 */
@Schema(description = "Diese Klasse beschreibt die Zuordnung eines Zeitrasters zu einem Planungsabschnitt.")
@TranspilerDTO
public class UVv1PlanungsabschnittZeitrasterExport {

	/** Die UV-ID des Zeitrasters. */
	@Schema(description = "die UV-ID des Zeitrasters", example = "4711")
	public long zeitrasterUvId = -1;

	/** Ein Array mit den IDs der zugeordneten Jahrgänge. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "ein Array mit den IDs der zugeordneten Jahrgänge"))
	public @NotNull List<Long> idsJahrgaenge = new ArrayList<>();

}
