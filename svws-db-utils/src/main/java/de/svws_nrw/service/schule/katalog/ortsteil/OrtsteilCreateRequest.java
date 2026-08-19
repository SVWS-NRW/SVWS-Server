package de.svws_nrw.service.schule.katalog.ortsteil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrtsteilCreateRequest {

	/** Der Name des Ortsteils. */
	@Schema(description = "der Name des Ortsteils", example = "Sieglar")
	@NotBlank
	@Size(max = 30)
	@NoWhitespaces
	public String ortsteil;

	/** Die ID des zugehörigen Ortes. */
	@Schema(description = "die ID des zugehörigen Ortes", example = "4711")
	@NotNull
	public Long idOrt;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	@Max(Integer.MAX_VALUE)
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	@NotNull
	public Boolean istSichtbar;

	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean istAenderbar;

}
