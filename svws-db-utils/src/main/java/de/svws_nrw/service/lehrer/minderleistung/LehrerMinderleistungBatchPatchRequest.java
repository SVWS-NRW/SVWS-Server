package de.svws_nrw.service.lehrer.minderleistung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für die Minderleisungsstunden von Lehrern.
 */
@Schema(description = "Minderleistung bei Lehrerabschnittsdaten.")
public class LehrerMinderleistungBatchPatchRequest extends LehrerMinderleistungPatchRequest {


	/** Die ID des zu patchenden Eintrages - muss gesetzt werden. */
	@Schema(description = "Die ID des zu patchenden Eintrages - muss gesetzt werden.", example = "4711")
	@NotNull
	public Long id;

}
