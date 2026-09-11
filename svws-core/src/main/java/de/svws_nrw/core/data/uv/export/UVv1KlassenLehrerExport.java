package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse beschreibt die Zuordnung einer Lehrkraft zur Klassenleitung einer Klasse.
 */
@Schema(description = "Diese Klasse beschreibt die Zuordnung einer Lehrkraft zur Klassenleitung einer Klasse.")
@TranspilerDTO
public class UVv1KlassenLehrerExport {

	/** Die ID des Lehrers, der der Klasse als Klassenlehrer zugeordnet ist. */
	@Schema(description = "die ID des Lehrers, der der Klasse als Klassenlehrer zugeordnet ist", example = "3001")
	public long idLehrer = -1;

	/** Die Reihenfolge der Zuordnung (z. B. 1 = Klassenleitung, 2 = stellvertretende Klassenleitung). */
	@Schema(description = "die Reihenfolge der Zuordnung (z. B. 1 = Klassenleitung, 2 = stellvertretende Klassenleitung)", example = "1")
	public int reihenfolge = 1;

}
