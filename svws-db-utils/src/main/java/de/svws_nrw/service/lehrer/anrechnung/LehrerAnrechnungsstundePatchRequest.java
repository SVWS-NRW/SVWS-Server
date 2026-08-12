package de.svws_nrw.service.lehrer.anrechnung;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das Patch-DTO für die Anrechnungsstunden von Lehrern.
 */
@Schema(description = "Anrechnungsstunden bei Lehrerabschnittsdaten.")
public class LehrerAnrechnungsstundePatchRequest {

	/** Die ID des zu patchenden Eintrages - muss gesetzt werden. */
	@Schema(description = "Die ID des zu patchenden Eintrages - muss gesetzt werden.", example = "4711")
	@NotNull(message = "Die ID des zu patchenden Eintrages muss gesetzt werden.")
	public Long id;

	/** Die ID des Anrechnungsgrundes - darf nicht null gesetzt werden. */
	@Schema(description = "Die ID des Anrechnungsgrundes - darf nicht null gesetzt werden.", example = "4713")
	@NotNull(message = "Das Feld 'idGrund' darf nicht null sein.")
	public JsonNullable<Long> idGrund = JsonNullable.undefined();

	/** Die Anzahl der Anrechnungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden. */
	@Schema(description = "Die Anzahl der Anrechnungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden.", example = "0.5")
	@NotNull(message = "Das Feld 'anzahl' darf nicht null sein.")
	@Min(value = 0, message = "Die Anzahl der Anrechnungsstunden darf nicht negativ sein.")
	public JsonNullable<Double> anzahl = JsonNullable.undefined();

}
