package de.svws_nrw.core.data.enm;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zur Konfiguration
 * des ENM-Servers/Clients
 * Sie beschreibt ein Element diser Konfiguration
 */
@XmlRootElement
@Schema(description = "Ein Konfigurationselement.")
@TranspilerDTO
public class ENMServerConfigElement {

	/** Der Schlüssel des Konfigurationselements */
	@Schema(description = "der Schlüssel des Konfigurationselements.")
	public @NotNull String key = "";

	/** Der Wert des Konfigurationselements. */
	@Schema(description = "der Wert des Konfigurationselements.")
	public @NotNull String value = "";

	/** Der Typ des Konfigurationselements. */
	@Schema(description = "der Typ des Konfigurationselements.")
	public @NotNull String type = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMServerConfigElement() {
		// leer
	}

}
