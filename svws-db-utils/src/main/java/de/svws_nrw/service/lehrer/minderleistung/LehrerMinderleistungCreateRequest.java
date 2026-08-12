package de.svws_nrw.service.lehrer.minderleistung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen von Einträgen für die Minderleistungen von Lehrern.
 */

@Schema(description = "Minderleistungen bei Lehrerabschnittsdaten.")
public final class LehrerMinderleistungCreateRequest {

	/** Die ID der Lehrerabschnittsdaten - muss gesetzt werden. */
	@Schema(description = "Die ID der Lehrerabschnittsdaten - muss gesetzt werden.", example = "4712")
	@NotNull
	public Long idAbschnittsdaten;

	/** Die ID des Minderleistungsgrundes - darf nicht null gesetzt werden. */
	@Schema(description = "Die ID des Anrechnungsgrundes - darf nicht null gesetzt werden.", example = "4713")
	@NotNull
	public Long idGrund;

	/** Die Anzahl der Minderleistungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden. */
	@Schema(description = "Die Anzahl der Minderleistungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden.", example = "0.5")
	@NotNull
	@Min(0)
	public Double anzahl;

}
