package de.svws_nrw.service.gost.klausuren;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für eine GOSt-Klausurvorgabe.
 */
@Schema(description = "Patch-Daten für eine GOSt-Klausurvorgabe.")
public class GostKlausurenVorgabePatchRequest {

	/** Die ID der Klausurvorgabe. */
	@Schema(description = "die ID der Klausurvorgabe")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die Klausurdauer. */
	@Schema(description = "die Klausurdauer")
	public JsonNullable<Integer> dauer = JsonNullable.undefined();

	/** Die Auswahlzeit. */
	@Schema(description = "die Auswahlzeit")
	public JsonNullable<Integer> auswahlzeit = JsonNullable.undefined();

	/** Gibt an, ob eine GKL möglich ist. */
	@Schema(description = "gibt an, ob eine GKL möglich ist")
	public JsonNullable<Boolean> istGklMoeglich = JsonNullable.undefined();

	/** Gibt an, ob es sich um eine mündliche Prüfung handelt. */
	@Schema(description = "gibt an, ob es sich um eine mündliche Prüfung handelt")
	public JsonNullable<Boolean> istMdlPruefung = JsonNullable.undefined();

	/** Gibt an, ob Audio benötigt wird. */
	@Schema(description = "gibt an, ob Audio benötigt wird")
	public JsonNullable<Boolean> istAudioNotwendig = JsonNullable.undefined();

	/** Gibt an, ob Video benötigt wird. */
	@Schema(description = "gibt an, ob Video benötigt wird")
	public JsonNullable<Boolean> istVideoNotwendig = JsonNullable.undefined();

	/** Die Bemerkung zur Klausurvorgabe. */
	@Schema(description = "die Bemerkung zur Klausurvorgabe")
	public JsonNullable<String> bemerkungVorgabe = JsonNullable.undefined();

}
