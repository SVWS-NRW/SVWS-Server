package de.svws_nrw.core.data.db;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die konfigurierten Datenbank-Schemata.
 */
@XmlRootElement
@Schema(description = "ein Eintrag für das DB-Schema in der Liste der Schemata.")
@TranspilerDTO
public class DBSchemaListeEintrag {

	/** Der Name des Datenbank-Schemas. */
	@Schema(description = "der Name des Datenbank-Schemas", example = "svwsdb")
	public String name;

	/** Gibt an, ob es sich um das Default-Schema in der Konfiguration handelt. */
	@Schema(description = "gibt an, ob es sich um das Default-Schema in der Konfiguration handelt", example = "false")
	public boolean isDefault;

	/** Gibt an, ob es sich um ein deaktiviertes Schema in der Konfiguration handelt. */
	@Schema(description = "gibt an, ob es sich um ein deaktiviertes Schema in der Konfiguration handelt", example = "false")
	public boolean isDeactivated;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public DBSchemaListeEintrag() {
		// leer
	}

}
