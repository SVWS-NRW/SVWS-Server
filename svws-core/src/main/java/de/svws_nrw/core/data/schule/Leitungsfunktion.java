package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-Api-Schnittstelle verwendet.
 * Sie beschreibt wie die Daten der Leitungsfunktion übergeben werden.
 */

@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Leitungsfunktionen")
@TranspilerDTO
public class Leitungsfunktion {

	/** Die ID der Leitungsfunktion */
	@Schema (description = "Die ID der Leitungsfunktion", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Die Bezeichnung der Leitungsfunktion */
	@Schema(description = "Die Bezeichnung der Leitungsfunktion", example = "Schulverwaltung")
	public @NotNull String bezeichnung = "";

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung;

	/** Gibt an, ob die Telefonart in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob die Telefonart in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;


}
