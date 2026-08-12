package de.svws_nrw.service.lehrer.anrechnung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen von Einträgen für die Anrechnungsstunden von Lehrern.
 */
@Schema(description = "Anrechnungsstunden bei Lehrerabschnittsdaten.")
public class LehrerAnrechnungsstundeCreateRequest {

	/** Die ID der Lehrerabschnittsdaten - muss gesetzt werden. */
	@Schema(description = "Die ID der Lehrerabschnittsdaten - muss gesetzt werden.", example = "4712")
	@NotNull(message = "Die ID der Lehrerabschnittsdaten muss gesetzt werden.")
	public Long idAbschnittsdaten;

	/** Die ID des Anrechnungsgrundes - darf nicht null gesetzt werden. */
	@Schema(description = "Die ID des Anrechnungsgrundes - darf nicht null gesetzt werden.", example = "4713")
	@NotNull(message = "Die ID des Anrechnungsgrundes muss gesetzt werden.")
	public Long idGrund;

	/** Die Anzahl der Anrechnungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden. */
	@Schema(description = "Die Anzahl der Anrechnungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden.", example = "0.5")
	@Min(value = 0, message = "Die Anzahl der Anrechnungsstunden darf nicht negativ sein.")
	public double anzahl;

}
