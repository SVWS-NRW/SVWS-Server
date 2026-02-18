package de.svws_nrw.core.data.kataloge;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für den Katalog SchülerSchwerpunkte übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Schwerpunkte.")
@TranspilerDTO
public class SchuelerSchwerpunkt {

	/**
	 * Die ID des Schwerpunktes
	 */
	@Schema(description = "die ID des Schwerpunktes", example = "42")
	public long id = -1;

	/**
	 * Die Bezeichnung des Schwerpunktes.
	 */
	@Schema(description = "die Bezeichnung des Schwerpunktes", example = "naturwissenschaftlich-technisch")
	public String bezeichnung;

	/**
	 * Die Sortierung des Schwerpunktes.
	 */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für den Schwerpunkt an", example = "1")
	public int sortierung;

	/**
	 * Gibt an, ob der Schwerpunkt in der UI sichtbar ist.
	 */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/**
	 * Gibt an, ob der Schwerpunkt in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	@Schema(description = "Gibt an, ob der Schwerpunkt in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true",
			accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;

}
