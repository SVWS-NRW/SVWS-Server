package de.svws_nrw.service.gost.klausuren;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für einen GOSt-Klausurraum.
 */
@Schema(description = "Patch-Daten für einen GOSt-Klausurraum.")
public class GostKlausurenRaumPatchRequest {

	/** Die ID des zu patchenden Klausurraums. */
	@Schema(description = "die ID des zu patchenden Klausurraums")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die ID des Stundenplanraums. */
	@Schema(description = "die ID des Stundenplanraums")
	public JsonNullable<Long> idStundenplanRaum = JsonNullable.undefined();

	/** Die Bemerkung. */
	@Schema(description = "die Bemerkung")
	public JsonNullable<String> bemerkung = JsonNullable.undefined();

}
