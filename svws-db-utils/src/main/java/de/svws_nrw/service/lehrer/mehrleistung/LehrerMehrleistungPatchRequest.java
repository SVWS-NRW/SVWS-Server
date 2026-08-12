package de.svws_nrw.service.lehrer.mehrleistung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für die Mehrleistungen von Lehrern.
 */
@Schema(description = "Mehrleistung bei Lehrerabschnittsdaten.")
public final class LehrerMehrleistungPatchRequest {

	/** Die ID des zu patchenden Eintrages - muss gesetzt werden. */
	@Schema(description = "Die ID des zu patchenden Eintrages - muss gesetzt werden.", example = "4711")
	@NotNull
	public Long id;

	/** Die ID des Mehrleistungsgrundes - darf nicht null gesetzt werden. */
	@Schema(description = "Die ID der Anrechnungsgrundes - darf nicht null gesetzt werden.", example = "4713")
	@NotNull
	public JsonNullable<Long> idGrund = JsonNullable.undefined();

	/** Die Anzahl der Mehrleistungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden. */
	@Schema(description = "Die Anzahl der Mehrleistungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden.", example = "0.5")
	@NotNull
	@Min(0)
	public JsonNullable<Double> anzahl = JsonNullable.undefined();

}
