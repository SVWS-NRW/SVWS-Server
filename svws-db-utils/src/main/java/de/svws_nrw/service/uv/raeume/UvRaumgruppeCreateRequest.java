package de.svws_nrw.service.uv.raeume;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Raumgruppe.
 */
@Schema(description = "die Informationen zum Erstellen einer UV-Raumgruppe.")
public class UvRaumgruppeCreateRequest {

	/** Die Bezeichnung der Raumgruppe. */
	@Schema(description = "Die Bezeichnung der Raumgruppe.")
	@NotNull(message = "Die Bezeichnung der Raumgruppe muss gesetzt werden.")
	@Size(max = 20, message = "Die Bezeichnung der Raumgruppe darf maximal 20 Zeichen lang sein.")
	public String bezeichnung;

	/** Das Datum, ab dem die Raumgruppe gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem die Raumgruppe gültig ist (ISO-Format yyyy-MM-dd).")
	@NotNull(message = "Das Datum 'gueltigVon' muss gesetzt werden.")
	@ValidDateFormat
	public String gueltigVon;

	/** Das Datum, bis zu dem die Raumgruppe gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem die Raumgruppe gültig ist (ISO-Format yyyy-MM-dd).")
	@ValidDateFormat
	public String gueltigBis;

	/** Eine Beschreibung der Raumgruppe. */
	@Schema(description = "Eine Beschreibung der Raumgruppe.")
	public String beschreibung;

}
