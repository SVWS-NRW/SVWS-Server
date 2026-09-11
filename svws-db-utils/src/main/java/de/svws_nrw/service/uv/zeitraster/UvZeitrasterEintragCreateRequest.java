package de.svws_nrw.service.uv.zeitraster;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines UV-Zeitraster-Eintrags.
 */
@Schema(description = "die Informationen zum Erstellen eines UV-Zeitraster-Eintrags.")
public class UvZeitrasterEintragCreateRequest {

	/** Die ID des Zeitrasters. */
	@Schema(description = "Die ID des Zeitrasters.")
	@NotNull(message = "Die ID des Zeitrasters muss gesetzt werden.")
	public Long idZeitraster;

	/** Der Wochentag (1=Montag, ...). */
	@Schema(description = "Der Wochentag.")
	@NotNull(message = "Der Wochentag muss gesetzt werden.")
	public Integer wochentag;

	/** Die Stunde. */
	@Schema(description = "Die Stunde.")
	@NotNull(message = "Die Stunde muss gesetzt werden.")
	public Integer stunde;

	/** Der Beginn (Minuten ab Mitternacht). */
	@Schema(description = "Der Beginn (Minuten ab Mitternacht).")
	@NotNull(message = "Der Beginn muss gesetzt werden.")
	public Integer beginn;

	/** Das Ende (Minuten ab Mitternacht). */
	@Schema(description = "Das Ende (Minuten ab Mitternacht).")
	@NotNull(message = "Das Ende muss gesetzt werden.")
	public Integer ende;

}
