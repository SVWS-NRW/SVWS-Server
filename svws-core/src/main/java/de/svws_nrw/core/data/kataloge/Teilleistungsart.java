package de.svws_nrw.core.data.kataloge;


import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für den Katalog Teilleistungsarten übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Teilleistungsarten.")
@TranspilerDTO
public final class Teilleistungsart {

	/**
	 * Die ID des Teilleistungsart
	 */
	@Schema(description = "die ID des Teilleistungsart", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/**
	 * Die Bezeichnung der Teilleistungsart.
	 */
	@Schema(description = "die Bezeichnung des Teilleistungsart", example = "Sek1-Somi1")
	public String bezeichnung;

	/**
	 * Die Sortierung des Teilleistungsart.
	 */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Teilleistungsart an", example = "1")
	public int sortierung;

	/**
	 * Gibt an, ob der Teilleistungsarten in der UI sichtbar ist.
	 */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/**
	 * Gibt an, ob der Teilleistungsarten in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	@Schema(description = "Gibt an, ob die Teilleistungsart in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true",
			accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;

}
