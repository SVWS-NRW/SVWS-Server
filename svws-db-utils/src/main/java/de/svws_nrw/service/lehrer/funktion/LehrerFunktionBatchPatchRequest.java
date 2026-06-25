package de.svws_nrw.service.lehrer.funktion;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Batch-Patch-DTO für die Funktionen von Lehrern.
 */
public class LehrerFunktionBatchPatchRequest extends LehrerFunktionPatchRequest {

	/** Die ID der Lehrerfunktion. */
	@Schema(description = "Die ID der Lehrerfunktion.", example = "4711")
	@NotNull
	public Long id;

}
