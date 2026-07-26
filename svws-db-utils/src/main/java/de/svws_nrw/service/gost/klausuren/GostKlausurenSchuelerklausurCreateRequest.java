package de.svws_nrw.service.gost.klausuren;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer GOSt-Schülerklausur.
 */
@Schema(description = "die Informationen zum Erstellen einer GOSt-Schülerklausur.")
public class GostKlausurenSchuelerklausurCreateRequest {

	/** Die ID der Kursklausur. */
	@Schema(description = "die ID der Kursklausur")
	@NotNull(message = "Die Kursklausur muss gesetzt werden.")
	public Long idKursklausur;

	/** Die ID des Schülers. */
	@Schema(description = "die ID des Schülers")
	@NotNull(message = "Der Schüler muss gesetzt werden.")
	public Long idSchueler;

	/** Gibt an, ob die Schülerklausur aktiv ist. */
	@Schema(description = "gibt an, ob die Schülerklausur aktiv ist")
	public Boolean aktiv;

	/** Die Bemerkung. */
	@Schema(description = "die Bemerkung")
	public JsonNullable<String> bemerkung = JsonNullable.undefined();

}
