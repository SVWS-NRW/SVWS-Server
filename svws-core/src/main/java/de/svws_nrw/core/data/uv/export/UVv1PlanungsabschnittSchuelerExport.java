package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse beschreibt die Zuordnung eines Schülers zu einem Planungsabschnitt.
 */
@Schema(description = "Diese Klasse beschreibt die Zuordnung eines Schülers zu einem Planungsabschnitt.")
@TranspilerDTO
public class UVv1PlanungsabschnittSchuelerExport {

	/** Die ID des Schülers. */
	@Schema(description = "die ID des Schülers", example = "4711")
	public long idSchueler = -1;

	/** Die ID des Jahrgangs, dem der Schüler zugeordnet ist. */
	@Schema(description = "die ID des Jahrgangs", example = "1")
	public long idJahrgang = -1;

	/** Die UV-ID der Klasse, der der Schüler zugeordnet ist. */
	@Schema(description = "die UV-ID der Klasse", example = "10")
	public Long klasseUvId = null;

}
