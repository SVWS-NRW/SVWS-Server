package de.svws_nrw.service.schule.katalog.ort;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OrtCreateRequest {

	/** Die Postleitzahl. */
	@Schema(description = "die Postleitzahl", example = "53840")
	@NotBlank
	@Size(max = 10)
	@NoWhitespaces
	public String plz;

	/** Der Name des Ortes. */
	@Schema(description = "der Name des Ortes", example = "Troisdorf")
	@NotBlank
	@Size(max = 50)
	@NoLeadingOrTrailingWhitespaces
	public String ortsname;

	/** Der Name des Kreises. */
	@Schema(description = "der Name des Kreises", example = "RSK")
	@Size(max = 30)
	public String kreis;

	/** Die ID des Bundeslandes. (CoreType: Laender) */
	@Schema(description = "Die ID des Bundeslandes. (CoreType: Laender)", example = "1090")
	public Long idBundesland;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	@Max(Integer.MAX_VALUE)
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example = "true")
	public boolean istAenderbar;

}
