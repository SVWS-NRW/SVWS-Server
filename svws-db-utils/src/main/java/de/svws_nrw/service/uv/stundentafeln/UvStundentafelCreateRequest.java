package de.svws_nrw.service.uv.stundentafeln;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Stundentafel.
 */
@Schema(description = "die Informationen zum Erstellen einer UV-Stundentafel.")
public class UvStundentafelCreateRequest {

	/** Die ID des Jahrgangs (Fremdschlüssel). */
	@Schema(description = "Die ID des Jahrgangs.")
	@NotNull(message = "Die ID des Jahrgangs muss gesetzt werden.")
	public Long idJahrgang;

	/** Die Bezeichnung der Stundentafel. */
	@Schema(description = "Die Bezeichnung der Stundentafel.")
	@NotNull(message = "Die Bezeichnung muss gesetzt werden.")
	public String bezeichnung;

	/** Das Datum, ab dem die Stundentafel gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem die Stundentafel gültig ist (ISO-Format yyyy-MM-dd).")
	@NotNull(message = "Das Datum 'gueltigVon' muss gesetzt werden.")
	public String gueltigVon;

	/** Das Datum, bis zu dem die Stundentafel gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem die Stundentafel gültig ist (ISO-Format yyyy-MM-dd).")
	public String gueltigBis;

	/** Eine optionale Beschreibung. */
	@Schema(description = "Eine optionale Beschreibung.")
	public String beschreibung;

}
