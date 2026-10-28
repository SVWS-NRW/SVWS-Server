package de.svws_nrw.service.schule.katalog.einwilligungsart;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Der Request zum Erstellen einer neuen Einwilligungsart im Katalog.
 */
public class EinwilligungsartCreateRequest {

	/** Die Bezeichnung der Einwilligungsart */
	@Schema(description = "Die Bezeichnung der Einwilligungsart", example = "Verwendung Foto")
	@NotBlank
	@Size(max = 250)
	@NoLeadingOrTrailingWhitespaces
	public String bezeichnung;

	/** Die ID des Personentyps, für den die Einwilligungsart gilt */
	@Schema(description = "Die ID des Personentyps, für den die Einwilligungsart gilt", example = "2")
	@NotNull
	public Integer idPersonTyp;

	/** Der Schlüssel der Einwilligungsart, ein leerer Schlüssel wird nicht gespeichert */
	@Schema(description = "Der Schlüssel der Einwilligungsart", example = "FOTO")
	@Size(max = 20)
	@NoWhitespaces
	public String schluessel;

	/** Eine ausführliche Beschreibung der Einwilligungsart */
	@Schema(description = "Eine ausführliche Beschreibung der Einwilligungsart", example = "Einwilligung zur Verwendung von Fotos")
	public String beschreibung;

	/** Die Position in der Sortierreihenfolge der Katalog-Einträge */
	@Schema(description = "Die Position in der Sortierreihenfolge der Katalog-Einträge", example = "32000")
	public int sortierung = 32000;

	/** Gibt an, ob die Einwilligungsart in der Anwendung sichtbar ist */
	@Schema(description = "Gibt an, ob die Einwilligungsart in der Anwendung sichtbar ist", example = "true")
	public boolean istSichtbar = true;

}
