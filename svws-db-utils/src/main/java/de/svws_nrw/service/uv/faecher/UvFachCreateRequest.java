package de.svws_nrw.service.uv.faecher;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines UV-Fachs.
 */
@Schema(description = "die Informationen zum Erstellen eines UV-Fachs.")
public class UvFachCreateRequest {

	/** Die ID des Faches (Fremdschlüssel auf die Tabelle EigeneSchule_Faecher). */
	@Schema(description = "Die ID des Faches.")
	@NotNull(message = "Die ID des Faches muss gesetzt werden.")
	public Long idFach;

	/** Das Datum, ab dem das Fach gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem das Fach gültig ist (ISO-Format yyyy-MM-dd).")
	@NotNull(message = "Das Datum 'gueltigVon' muss gesetzt werden.")
	public String gueltigVon;

	/** Das Datum, bis zu dem das Fach gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem das Fach gültig ist (ISO-Format yyyy-MM-dd).")
	public String gueltigBis;

}
