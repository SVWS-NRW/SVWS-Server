package de.svws_nrw.core.data.db;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beinhaltet den Namen und die Versionsinformationen eines SVWS-Datenbank-Schemas.
 */
@XmlRootElement
@Schema(description = "ein Eintrag eines SVWS-Schemas in der SVWS-Schema-Liste.")
@TranspilerDTO
public class SchemaListeEintrag {

	/** Der Name des Schemas. */
	@Schema(description = "der Name des Schemas", example = "svwsdb")
	public @NotNull String name = "";

	/** Der Name des Datenbank-Benutzers für das Schema. */
	@Schema(description = "der Name des Datenbank-Benutzers für das Schema", example = "svwsdb")
	public @NotNull String username = "";

	/** Gibt an, ob das Schema ein SVWS-Schema ist oder nicht. */
	@Schema(description = "gibt an, ob das Schema ein SVWS-Schema ist oder nicht", example = "false")
	public boolean isSVWS = false;

	/** Die Revisionsnummer des Schemas. */
	@Schema(description = "die Revisionsnummer des Schemas", example = "3")
	public long revision = -1;

	/** Gibt an, ob das Schema als "verdorben" markiert wurde und deswegen nicht mehr für den produktiven Einsatz in der Schule genutzt werden sollte. */
	@Schema(description = "gibt an, ob das Schema als \"verdorben\" markiert wurde und deswegen nicht mehr für den produktiven Einsatz in der Schule genutzt werden sollte",
			example = "false")
	public boolean isTainted = false;

	/** Gibt an, ob das Schema in der Konfiguration des aktuellen SVWS-Servers eingetragen ist. */
	@Schema(description = "gibt an, ob das Schema in der Konfiguration des aktuellen SVWS-Servers eingetragen ist.", example = "true")
	public boolean isInConfig = false;

	/** Gibt an, ob das Schema in der Konfiguration des aktuellen SVWS-Servers aufgrund von Fehlern deaktiviert ist. */
	@Schema(description = "gibt an, ob das Schema in der Konfiguration des aktuellen SVWS-Servers aufgrund von Fehlern deaktiviert ist.", example = "true")
	public boolean isDeactivated = false;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchemaListeEintrag() {
		// leer
	}

}
