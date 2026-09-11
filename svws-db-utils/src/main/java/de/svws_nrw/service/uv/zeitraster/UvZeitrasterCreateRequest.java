package de.svws_nrw.service.uv.zeitraster;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines UV-Zeitrasters.
 */
@Schema(description = "die Informationen zum Erstellen eines UV-Zeitrasters.")
public class UvZeitrasterCreateRequest {

	/** Die Bezeichnung des Zeitrasters. */
	@Schema(description = "Die Bezeichnung des Zeitrasters.")
	@NotNull(message = "Die Bezeichnung muss gesetzt werden.")
	public String bezeichnung;

	/** Das Datum, ab dem das Zeitraster gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem das Zeitraster gültig ist (ISO-Format yyyy-MM-dd).")
	@NotNull(message = "Das Datum 'gueltigVon' muss gesetzt werden.")
	public String gueltigVon;

	/** Das Datum, bis zu dem das Zeitraster gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem das Zeitraster gültig ist (ISO-Format yyyy-MM-dd).")
	public String gueltigBis;

}
