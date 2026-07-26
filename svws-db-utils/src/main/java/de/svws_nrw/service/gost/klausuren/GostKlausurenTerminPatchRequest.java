package de.svws_nrw.service.gost.klausuren;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für einen GOSt-Klausurtermin.
 */
@Schema(description = "Patch-Daten für einen GOSt-Klausurtermin.")
public class GostKlausurenTerminPatchRequest {

	/** Die ID des zu patchenden Klausurtermins. */
	@Schema(description = "die ID des zu patchenden Klausurtermins")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Das Quartal. */
	@Schema(description = "das Quartal")
	public JsonNullable<Integer> quartal = JsonNullable.undefined();

	/** Die Bemerkung. */
	@Schema(description = "die Bemerkung")
	public JsonNullable<String> bemerkung = JsonNullable.undefined();

	/** Die Bezeichnung. */
	@Schema(description = "die Bezeichnung")
	public JsonNullable<String> bezeichnung = JsonNullable.undefined();

	/** Das Datum. */
	@Schema(description = "das Datum")
	public JsonNullable<String> datum = JsonNullable.undefined();

	/** Die Startzeit in Minuten. */
	@Schema(description = "die Startzeit in Minuten")
	public JsonNullable<Integer> startzeit = JsonNullable.undefined();

	/** Gibt an, ob Nachschreiber zugelassen sind. */
	@Schema(description = "gibt an, ob Nachschreiber zugelassen sind")
	public JsonNullable<Boolean> nachschreiberZugelassen = JsonNullable.undefined();

}
