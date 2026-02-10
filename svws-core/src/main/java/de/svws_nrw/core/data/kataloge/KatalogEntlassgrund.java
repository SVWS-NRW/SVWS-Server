package de.svws_nrw.core.data.kataloge;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt wie die Daten des Entlassgrundes übergeben werden.
 */
@XmlRootElement
@Schema(description = "Der Entlassgrund eines Schulbesuchs")
@TranspilerDTO
public class KatalogEntlassgrund {

	/** Die ID des Entlassgrundes */
	@Schema(description = "Die ID des Entlassgrundes", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Bezeichnung des Entlassgrundes */
	@Schema(description = "Die Bezeichnung des Entlassgrundes", example = "Schulpflicht endet")
	public @NotNull String bezeichnung = "";

	/** Die Sortierreihenfolge des Entlassgrund-Eintrags */
	@Schema(description = "Die Sortierreihenfolge des Entlassgrund-Eintrags", example = "1")
	public int sortierung = 32000;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll. */
	@Schema(description = "Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll.", example = "true")
	public boolean istSichtbar = true;


	/** Gibt an, ob der Entlassgrund in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob der Entlassgrund in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public Boolean referenziertInAnderenTabellen = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public KatalogEntlassgrund() {
		// leer
	}

}
