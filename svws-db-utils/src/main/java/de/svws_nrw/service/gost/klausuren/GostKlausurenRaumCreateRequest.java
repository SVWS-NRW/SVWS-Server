package de.svws_nrw.service.gost.klausuren;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines GOSt-Klausurraums.
 */
@Schema(description = "die Informationen zum Erstellen eines GOSt-Klausurraums.")
public class GostKlausurenRaumCreateRequest {

	/** Die ID des Klausurtermins. */
	@Schema(description = "die ID des Klausurtermins")
	@NotNull(message = "Der Klausurtermin muss gesetzt werden.")
	public Long idTermin;

	/** Die ID des Stundenplanraums. */
	@Schema(description = "die ID des Stundenplanraums")
	public JsonNullable<Long> idStundenplanRaum = JsonNullable.undefined();

	/** Die Bemerkung. */
	@Schema(description = "die Bemerkung")
	public JsonNullable<String> bemerkung = JsonNullable.undefined();

}
