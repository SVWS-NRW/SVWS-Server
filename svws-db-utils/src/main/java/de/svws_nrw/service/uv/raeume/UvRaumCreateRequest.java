package de.svws_nrw.service.uv.raeume;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines UV-Raumes.
 */
@Schema(description = "die Informationen zum Erstellen eines UV-Raumes.")
public class UvRaumCreateRequest {

	/** Das Kürzel des Raums. */
	@Schema(description = "Das Kürzel des Raums.")
	@NotNull(message = "Das Kürzel des Raums muss gesetzt werden.")
	@Size(max = 20, message = "Das Kürzel des Raums darf maximal 20 Zeichen lang sein.")
	public String kuerzel;

	/** Die Beschreibung des Raums. */
	@Schema(description = "Die Beschreibung des Raums.")
	public String beschreibung;

	/** Die Größe des Raums an Arbeitsplätzen für Schüler. */
	@Schema(description = "Die Größe des Raums an Arbeitsplätzen für Schüler.")
	@NotNull(message = "Die Größe des Raums muss gesetzt werden.")
	public Integer groesse;

	/** Die ID der Raumgruppe, falls der Raum zu einer solchen gehört. */
	@Schema(description = "Die ID der Raumgruppe, falls der Raum zu einer solchen gehört.")
	public Long idRaumgruppe;

	/** Das Datum, ab dem der Raum gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem der Raum gültig ist (ISO-Format yyyy-MM-dd).")
	@NotNull(message = "Das Datum 'gueltigVon' muss gesetzt werden.")
	@ValidDateFormat
	public String gueltigVon;

	/** Das Datum, bis zu dem der Raum gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem der Raum gültig ist (ISO-Format yyyy-MM-dd).")
	@ValidDateFormat
	public String gueltigBis;

}
