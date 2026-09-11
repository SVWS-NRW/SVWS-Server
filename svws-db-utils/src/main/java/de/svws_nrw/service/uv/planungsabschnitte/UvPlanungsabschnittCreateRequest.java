package de.svws_nrw.service.uv.planungsabschnitte;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines UV-Planungsabschnitts.
 */
@Schema(description = "die Informationen zum Erstellen eines UV-Planungsabschnitts.")
public class UvPlanungsabschnittCreateRequest {

	/** Das Schuljahr des Planungsabschnitts (z.B. 2025 für 2025/26). */
	@Schema(description = "Das Schuljahr des Planungsabschnitts.")
	@NotNull(message = "Das Schuljahr muss gesetzt werden.")
	public Integer schuljahr;

	/** Gibt an, ob der Planungsabschnitt aktiv ist. */
	@Schema(description = "Gibt an, ob der Planungsabschnitt aktiv ist.")
	public Boolean aktiv;

	/** Das Datum, ab dem der Planungsabschnitt gültig ist. */
	@Schema(description = "Das Datum, ab dem der Planungsabschnitt gültig ist.")
	@NotNull(message = "Das Datum 'gueltigVon' muss gesetzt werden.")
	public String gueltigVon;

	/** Das Datum, bis zu dem der Planungsabschnitt gültig ist. */
	@Schema(description = "Das Datum, bis zu dem der Planungsabschnitt gültig ist.")
	public String gueltigBis;

	/** Optionale Beschreibung zum Planungsabschnitt. */
	@Schema(description = "Optionale Beschreibung zum Planungsabschnitt.")
	public String beschreibung;

}
