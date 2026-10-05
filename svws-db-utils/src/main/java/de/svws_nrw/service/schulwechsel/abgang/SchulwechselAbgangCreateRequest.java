package de.svws_nrw.service.schulwechsel.abgang;

import de.svws_nrw.validation.constraints.ValidDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class SchulwechselAbgangCreateRequest {

	/** Die Id des Schülerdatensatzes auf den sich der Wechselvorgang bezieht. */
	@Schema(description = "Die Id des Schülers auf den sich der Wechselvorgang bezieht", example = "4423")
	@NotNull
	public Long idSchueler;

	/** Die Id des aktuellen Status. */
	@Schema(description = "Die Id des aktuellen Status", example = "1")
	@NotNull
	public Integer idStatus;

	/** Der Zeitpunkt der letzten Statusänderung des Wechselvorgangs. */
	@Schema(description = "Der Zeitpunkt der letzten Änderung des Dokuments", example = "2025-04-12 12:34:25")
	@ValidDateTime
	@NotNull
	public String lastModified;

	/** Die Id des zugehörigen Wechseldokuments. */
	@Schema(description = "Die Id des zugehörigen Wechseldokuments", example = "12345")
	public Long idDocument;

	/** Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang. */
	@Schema(description = "Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang", example = "123e4567-e89b-12d3-a456-426614174000")
	public String idSchulkindSchulbewerbung;
}
