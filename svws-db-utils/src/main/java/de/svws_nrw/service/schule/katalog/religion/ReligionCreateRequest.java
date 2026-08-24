package de.svws_nrw.service.schule.katalog.religion;


import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReligionCreateRequest {

	/** Die Bezeichnung bzw. der Name der Religion. */
	@Schema(description = "die Bezeichnung bzw. der Name der Religion", example = "röm.-kath.")
	@NotBlank
	@Size(max = 30)
	@NoLeadingOrTrailingWhitespaces
	public String bezeichnung;

	/** Die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint. */
	@Schema(description = "die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint", example = "katholisch")
	@Size(max = 50)
	@NoLeadingOrTrailingWhitespaces
	public String bezeichnungZeugnis;

	/** Die ID des Eintrages für die Statistik. */
	@Schema(description = "Die ID des Eintrages für die Statistik.", example = "1000")
	@NotNull
	public Long idReligion;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	@NotNull
	public Integer sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	@NotNull
	public Boolean istSichtbar;

}
