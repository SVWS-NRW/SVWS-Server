package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für die standardisierten Katalogeinträge übergeben werden.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Fahrschülerarten")
@TranspilerDTO
public class Fahrschuelerart {

	/** Die ID der Fahrschülerart. */
	@Schema(description = "Die ID der Fahrschülerart", example = "4711")
	public long id;

	/** Die Bezeichnung der Fahrschülerart. */
	@Schema(description = "Die Bezeichnung der Fahrschülerart.", example = "Hier steht eine textuelle Beschreibung der Fahrschülerart.")
	public String bezeichnung = "";

	/** gibt an, ob die Fahrschülerart in der Anwendung sichtbar sein soll oder nicht */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = true;

	/** Gibt an, ob die Fahrschülerart in der Anwendung änderbar sein soll oder nicht. */
	@Schema(description = "Gibt an, ob die Fahrschülerart in der Anwendung änderbar sein soll oder nicht.", example = "true")
	public boolean istAenderbar = true;

	/** Gibt die Position in der Sortierreihenfolge für die Fahrschülerarten an. */
	@Schema(description = "Gibt die Position in der Sortierreihenfolge für die Fahrschülerarten an. ", example = "1")
	public int sortierung = 1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Fahrschuelerart() {
		// leer
	}
}
