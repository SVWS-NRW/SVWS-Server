package de.svws_nrw.service.schule.katalog.ortsteil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrtsteilPatchRequest {

	/** Der Name des Ortsteils. */
	@Schema(description = "der Name des Ortsteils", example = "Sieglar")
	public JsonNullable<@NotBlank @Size(max = 30) @NoWhitespaces String> ortsteil = JsonNullable.undefined();

	/** Die ID des zugehörigen Ortes. */
	@Schema(description = "die ID des zugehörigen Ortes", example = "4711")
	public JsonNullable<@NotNull Long> idOrt = JsonNullable.undefined();

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public JsonNullable<@NotNull @Max(Integer.MAX_VALUE)Integer> sortierung = JsonNullable.undefined();

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public JsonNullable<@NotNull Boolean> istAenderbar = JsonNullable.undefined();

}
