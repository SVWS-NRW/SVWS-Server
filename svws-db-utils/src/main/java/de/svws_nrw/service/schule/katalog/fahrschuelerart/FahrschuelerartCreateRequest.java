package de.svws_nrw.service.schule.katalog.fahrschuelerart;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Der Request zum Erstellen einer neuen Fahrschülerart im Katalog.
 */
public class FahrschuelerartCreateRequest {

	/** Die Bezeichnung der Fahrschülerart */
	@Schema(description = "Die Bezeichnung der Fahrschülerart", example = "Bus")
	@NotBlank
	@Size(max = 30)
	@NoLeadingOrTrailingWhitespaces
	public String bezeichnung;

	/** Die Position in der Sortierreihenfolge der Katalog-Einträge */
	@Schema(description = "Die Position in der Sortierreihenfolge der Katalog-Einträge", example = "32000")
	public int sortierung = 32000;

	/** Gibt an, ob die Fahrschülerart in der Anwendung sichtbar ist */
	@Schema(description = "Gibt an, ob die Fahrschülerart in der Anwendung sichtbar ist", example = "true")
	public boolean istSichtbar = true;

	/** Gibt an, ob die Fahrschülerart in der Anwendung änderbar ist */
	@Schema(description = "Gibt an, ob die Fahrschülerart in der Anwendung änderbar ist", example = "true")
	public boolean istAenderbar = true;
}
