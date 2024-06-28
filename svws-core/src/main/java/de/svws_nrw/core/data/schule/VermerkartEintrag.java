package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt den schulspezifischen Katalog der Vermerkarten.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der schulspezifischen Vermerkarten.")
@TranspilerDTO
public class VermerkartEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Die Bezeichnung der Vermerkart. */
	@Schema(description = "die Bezeichnung der Vermerkart", example = "Ganztagsbetreuung")
	public @NotNull String bezeichnung = "";

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung = 1;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = true;

	/** Gibt an wie viele Vermerke dem entsprechenden Vermerkart-Eintrag zugeordnet sind (berechneter Wert). */
	@Schema(description = "gibt an wie viele Vermerke dem entsprechenden Vermerkart-Eintrag zugeordnet sind (berechneter Wert)", example = "3")
	public int anzahlVermerke;

}
