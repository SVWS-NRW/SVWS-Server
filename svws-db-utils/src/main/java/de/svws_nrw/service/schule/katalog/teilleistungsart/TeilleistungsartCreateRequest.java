package de.svws_nrw.service.schule.katalog.teilleistungsart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TeilleistungsartCreateRequest {

	/**
	 * Die Bezeichnung der Teilleistungsart.
	 */
	@Schema(description = "die Bezeichnung des Teilleistungsart", example = "Sek1-Somi1")
	public @NotNull @Size(min = 1, max = 50) String bezeichnung;

	/**
	 * Die Sortierung des Teilleistungsart.
	 */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Teilleistungsart an", example = "1")
	public @Max(32000) int sortierung;

	/**
	 * Gibt an, ob der Teilleistungsarten in der UI sichtbar ist.
	 */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

}
