package de.svws_nrw.service.uv.stundentafeln;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines UV-Stundentafel-Fachs.
 */
@Schema(description = "die Informationen zum Erstellen eines UV-Stundentafel-Fachs.")
public class UvStundentafelFachCreateRequest {

	/** Die ID der Stundentafel. */
	@Schema(description = "Die ID der Stundentafel.")
	@NotNull(message = "Die ID der Stundentafel muss gesetzt werden.")
	public Long idStundentafel;

	/** Der Abschnitt. */
	@Schema(description = "Der Abschnitt.")
	@NotNull(message = "Der Abschnitt muss gesetzt werden.")
	public Integer abschnitt;

	/** Die ID des Faches. */
	@Schema(description = "Die ID des Faches.")
	@NotNull(message = "Die ID des Faches muss gesetzt werden.")
	public Long idFach;

	/** Die Anzahl der Wochenstunden. */
	@Schema(description = "Die Anzahl der Wochenstunden.")
	@NotNull(message = "Die Wochenstunden müssen gesetzt werden.")
	public Double wochenstunden;

	/** Anzahl der davon enthaltenen Ergänzungsstunden. */
	@Schema(description = "Anzahl der davon enthaltenen Ergänzungsstunden.")
	public Double davonErgaenzungsstunden;

}
