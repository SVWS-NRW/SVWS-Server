package de.svws_nrw.service.gost.klausuren;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für eine GOSt-Kursklausur.
 */
@Schema(description = "Patch-Daten für eine GOSt-Kursklausur.")
public class GostKlausurenKursklausurPatchRequest {

	/** Die ID der zu patchenden Kursklausur. */
	@Schema(description = "die ID der zu patchenden Kursklausur")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die ID des Klausurtermins. */
	@Schema(description = "die ID des Klausurtermins")
	public JsonNullable<Long> idTermin = JsonNullable.undefined();

	/** Die Startzeit in Minuten. */
	@Schema(description = "die Startzeit in Minuten")
	public JsonNullable<Integer> startzeit = JsonNullable.undefined();

	/** Die Bemerkung. */
	@Schema(description = "die Bemerkung")
	public JsonNullable<String> bemerkung = JsonNullable.undefined();

}
