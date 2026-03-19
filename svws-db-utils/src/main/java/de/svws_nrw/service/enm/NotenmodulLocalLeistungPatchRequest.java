package de.svws_nrw.service.enm;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Die Informationenen zur Aktualisierung eines Leistungseintrages von Leistungsdaten im lokalen Notenmodul
 */
@Schema(description = "Die Informationenen zur Aktualisierung eines Leistungseintrages von Leistungsdaten im lokalen Notenmodul")
public class NotenmodulLocalLeistungPatchRequest {

	/** Die ID der Leistungsdaten des Schülers auf welche sich der Patch bezieht (z.B. 307956) */
	@Schema(description = "Die ID der Leistungsdaten des Schülers  auf welche sich der Patch bezieht.", example = "307956")
	public long id;

	/** Das Kürzel der Note, die vergeben wurde. */
	@Schema(description = "Das Kürzel der Note, die vergeben wurde.", example = "3+")
	public JsonNullable<String> note = JsonNullable.undefined();

	/** Das Kürzel der Quartals-Note, die vergeben wurde. */
	@Schema(description = "Das Kürzel der Quartals-Note, die vergeben wurde.", example = "3+")
	public JsonNullable<String> noteQuartal = JsonNullable.undefined();

	/** Gibt die Anzahl der gesamten Fehlstunden an, sofern diese fachbezogen ermittelt werden. */
	@Schema(description = "Gibt die Anzahl der gesamten Fehlstunden an, sofern diese fachbezogen ermittel werden.", example = "23")
	@Min(0)
	@Max(999)
	public JsonNullable<Integer> fehlstundenFach = JsonNullable.undefined();

	/** Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese fachbezogen ermittelt werden. */
	@Schema(description = "Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese fachbezogen ermittel werden.", example = "0")
	@Min(0)
	@Max(999)
	public JsonNullable<Integer> fehlstundenUnentschuldigtFach = JsonNullable.undefined();

	/** Die fachbezogenen Bemerkungen bzw. das Thema bei Projektkursen */
	@Schema(description = "Die fachbezogenen Bemerkungen bzw. das Thema bei Projektkursen.", example = "Text zum Fach")
	public JsonNullable<String> fachbezogeneBemerkungen = JsonNullable.undefined();

	/** Gibt an, ob ein Fach gemahnt wurde oder nicht. */
	@Schema(description = "Gibt an, ob ein Fach gemahnt wurde oder nicht.", example = "true")
	public JsonNullable<Boolean> istGemahnt = JsonNullable.undefined();

}
