package de.svws_nrw.service.lehrer.minderleistung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für die Minderleistungen von Lehrern.
 */
@Schema(description = "Minderleistung bei Lehrerabschnittsdaten.")
public class LehrerMinderleistungPatchRequest {


	/** Die ID des Minderleistungsgrundes - darf nicht null gesetzt werden. */
	@Schema(description = "Die ID der Anrechnungsgrundes - darf nicht null gesetzt werden.", example = "4713")
	public JsonNullable<@NotNull Long> idGrund = JsonNullable.undefined();

	/** Die Anzahl der Minderleistungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden. */
	@Schema(description = "Die Anzahl der Minderleistungsstunden, welche dem Grund zugeordnet sind - darf nicht null gesetzt werden.", example = "0.5")
	public JsonNullable<@NotNull @Min(0) Double> anzahl = JsonNullable.undefined();

}
