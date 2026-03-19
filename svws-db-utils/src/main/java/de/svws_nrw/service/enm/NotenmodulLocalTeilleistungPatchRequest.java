package de.svws_nrw.service.enm;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Die Informationenen zur Aktualisierung eines Teilleistungseintrages von Leistungsdaten im lokalen Notenmodul
 */
@Schema(description = "Die Informationenen zur Aktualisierung eines Teilleistungseintrages von Leistungsdaten im lokalen Notenmodul")
public class NotenmodulLocalTeilleistungPatchRequest {

	/** Die ID der Teilleistung des Schülers auf welche sich der Patch bezieht (z.B. 307956) */
	@Schema(description = "Die ID der Teilleistung des Schülers  auf welche sich der Patch bezieht.", example = "307956")
	public long id;

	/** Das Kürzel der Note, die vergeben wurde. */
	@Schema(description = "Das Kürzel der Note, die vergeben wurde.", example = "3+")
	public JsonNullable<String> note = JsonNullable.undefined();

}
