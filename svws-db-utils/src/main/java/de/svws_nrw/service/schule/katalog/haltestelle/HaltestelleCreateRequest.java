package de.svws_nrw.service.schule.katalog.haltestelle;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class HaltestelleCreateRequest {

	/** Die Bezeichnung der Haltestelle. */
	@Schema(description = "Die Bezeichnung der Haltestelle", example = "RE7 Hauptbahnhof")
	@NotBlank
	@Size(max = 30)
	@NoLeadingOrTrailingWhitespaces
	public String bezeichnung;

	/** Die Entfernung zwischen Schule und Haltestelle. */
	@Schema(description = "Die Entfernung zwischen Schule und Haltestelle", example = "7")
	public Double entfernungSchule;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public Integer sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public Boolean istSichtbar;

}
