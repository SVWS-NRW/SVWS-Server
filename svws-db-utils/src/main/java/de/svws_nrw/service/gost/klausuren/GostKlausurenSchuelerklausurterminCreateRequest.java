package de.svws_nrw.service.gost.klausuren;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines GOSt-Schülerklausurtermins.
 */
@Schema(description = "die Informationen zum Erstellen eines GOSt-Schülerklausurtermins.")
public class GostKlausurenSchuelerklausurterminCreateRequest {

	/** Die ID der Schülerklausur. */
	@Schema(description = "die ID der Schülerklausur")
	@NotNull(message = "Die Schülerklausur muss gesetzt werden.")
	public Long idSchuelerklausur;

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
