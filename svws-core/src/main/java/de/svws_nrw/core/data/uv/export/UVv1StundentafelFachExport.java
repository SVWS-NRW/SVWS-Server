package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse beschreibt ein Fach innerhalb einer Stundentafel im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt ein Fach innerhalb einer Stundentafel im UV-Export.")
@TranspilerDTO
public class UVv1StundentafelFachExport {

	/** Der Abschnitt des Schuljahres (z. B. 1 oder 2). */
	@Schema(description = "der Abschnitt des Schuljahres (z. B. 1 oder 2)", example = "1")
	public int abschnitt = 1;

	/** Die UV-ID des Faches. */
	@Schema(description = "die UV-ID des Faches", example = "4711")
	public long fachUvId = -1;

	/** Die Anzahl der Wochenstunden für das Fach. */
	@Schema(description = "die Anzahl der Wochenstunden für das Fach", example = "4.0")
	public double wochenstunden = 0.0;

	/** Die Anzahl der Ergänzungsstunden für das Fach (in den Wochenstunden enthalten). */
	@Schema(description = "die Anzahl der Ergänzungsstunden für das Fach (in den Wochenstunden enthalten)", example = "1.0")
	public double davonErgaenzungsstunden = 0.0;

}
