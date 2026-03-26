package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten den schulspezifischen Förderschwerpunkt-Katalog übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem schulspezifischen Förderschwerpunkt-Katalog.")
@TranspilerDTO
public class FoerderschwerpunktEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id;

	/** Das Kürzel des Eintrags. */
	@Schema(description = "das Kürzel des Eintrags", example = "EZ")
	public @NotNull String kuerzel = "";

	/** Das Kürzel des Eintrags im Rahmen der amtlichen Schulstatistik. */
	@Schema(description = "das Kürzel des Eintrags im Rahmen der amtlichen Schulstatisik", example = "EZ")
	public @NotNull String kuerzelStatistik = "";

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Die Sortierreihenfolge des Förderschwerpunkt-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Förderschwerpunkt-Eintrags", example = "1")
	public int sortierung;

	/** Gibt an, ob der Förderschwerpunkt in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob der Förderschwerpunkt in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public Boolean referenziertInAnderenTabellen = false;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public FoerderschwerpunktEintrag() {
		// leer
	}

}
