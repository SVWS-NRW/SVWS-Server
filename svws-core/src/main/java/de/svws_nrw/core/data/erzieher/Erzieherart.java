package de.svws_nrw.core.data.erzieher;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Erzieherart eines Erziehers.
 */
@XmlRootElement
@Schema(description = "Die Erzieherart eines Erziehers.")
@TranspilerDTO
public class Erzieherart {

	/** ID der Erzieherart */
	@Schema(description = "die ID der Erzieherart, welchem der Erzieher zugeordnet ist", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Bezeichnung der Erzieherart */
	@Schema(description = "die Bezeichnung der Erzieherart, welchem der Erzieher zugeordnet ist", example = "Mutter")
	public @NotNull String bezeichnung = "";

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung = 1;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = true;

	/** Exportbezeichnung der Erzieherart */
	@Schema(description = "die Bezeichnung der Erzieherart, welche exportiert wird", example = "Export")
	public String exportBez = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Erzieherart() {
		// leer
	}

}
