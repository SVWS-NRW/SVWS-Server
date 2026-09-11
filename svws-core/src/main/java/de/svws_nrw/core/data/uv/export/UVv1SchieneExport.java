package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse beschreibt eine Schiene innerhalb eines Planungsabschnitts im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt eine Schiene innerhalb eines Planungsabschnitts im UV-Export.")
@TranspilerDTO
public class UVv1SchieneExport {

	/** Die UV-ID der Schiene. */
	@Schema(description = "die UV-ID der Schiene", example = "4711")
	public long uvId = -1;

	/** Die laufende Nummer der Schiene innerhalb des Planungsabschnitts. */
	@Schema(description = "die laufende Nummer der Schiene innerhalb des Planungsabschnitts", example = "3")
	public int nummer = 0;

	/** Die Bezeichnung der Schiene. */
	@Schema(description = "die Bezeichnung der Schiene", example = "Schiene A")
	public String bezeichnung = null;

}
