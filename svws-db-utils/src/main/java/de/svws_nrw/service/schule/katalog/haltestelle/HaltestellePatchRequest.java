package de.svws_nrw.service.schule.katalog.haltestelle;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class HaltestellePatchRequest {

	/** Die Bezeichnung der Haltestelle. */
	@Schema(description = "Die Bezeichnung der Haltestelle", example = "RE7 Hauptbahnhof")
	public JsonNullable<@NotBlank @Size(max = 30) @NoLeadingOrTrailingWhitespaces String> bezeichnung = JsonNullable.undefined();

	/** Die Entfernung zwischen Schule und Haltestelle. */
	@Schema(description = "Die Entfernung zwischen Schule und Haltestelle", example = "7")
	public JsonNullable<Double> entfernungSchule = JsonNullable.undefined();

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public JsonNullable<@NotNull Integer> sortierung = JsonNullable.undefined();

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

}
