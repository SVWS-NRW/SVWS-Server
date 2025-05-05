package de.svws_nrw.core.data.schule;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für den schulspezifischen Katalog der Religionen
 * übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der schulspezifischen Religionen.")
@TranspilerDTO
public class ReligionEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Die Bezeichnung bzw. der Name der Religion. */
	@Schema(description = "die Bezeichnung bzw. der Name der Religion", example = "röm.-kath.")
	public @NotNull String bezeichnung = "";

	/** Die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint. */
	@Schema(description = "die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint", example = "katholisch")
	public String bezeichnungZeugnis = "";

	/** Das Kürzel des Eintrages für die Statistik. */
	@Schema(description = "das Kürzel des Eintrages für die Statistik", example = "KR")
	public String kuerzel = "";

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung = 1;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = true;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ReligionEintrag() {
		// leer
	}

}
