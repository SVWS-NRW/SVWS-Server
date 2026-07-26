package de.svws_nrw.service.gost.klausuren;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für eine GOSt-Schülerklausur.
 */
@Schema(description = "Patch-Daten für eine GOSt-Schülerklausur.")
public class GostKlausurenSchuelerklausurPatchRequest {

	/** Die ID der zu patchenden Schülerklausur. */
	@Schema(description = "die ID der zu patchenden Schülerklausur")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die Bemerkung. */
	@Schema(description = "die Bemerkung")
	public JsonNullable<String> bemerkung = JsonNullable.undefined();

	/** Gibt an, ob die Schülerklausur aktiv ist. */
	@Schema(description = "gibt an, ob die Schülerklausur aktiv ist")
	public JsonNullable<Boolean> aktiv = JsonNullable.undefined();

}
