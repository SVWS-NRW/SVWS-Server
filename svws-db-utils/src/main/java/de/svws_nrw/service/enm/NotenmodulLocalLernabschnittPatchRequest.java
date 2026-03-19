package de.svws_nrw.service.enm;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Die Informationenen zur Aktualisierung eines Lernabschnitts im lokalen Notenmodul
 */
@Schema(description = "Die Informationenen zur Aktualisierung eines Lernabschnitts im lokalen Notenmodul")
public class NotenmodulLocalLernabschnittPatchRequest {

	/** Die ID des Schüler-Lernabschnittes auf welchen sich der Patch bezieht (z.B. 307956) */
	@Schema(description = "Die ID des Schüler-Lernabschnittes auf welchen sich der Patch bezieht.", example = "307956")
	public long id;

	/** Gibt die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden. */
	@Schema(description = "Gibt die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittel werden.", example = "23")
	@Min(0)
	@Max(999)
	public JsonNullable<Integer> fehlstundenGesamt = JsonNullable.undefined();

	/** Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden. */
	@Schema(description = "Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittel werden.", example = "0")
	@Min(0)
	@Max(999)
	public JsonNullable<Integer> fehlstundenGesamtUnentschuldigt = JsonNullable.undefined();

}
