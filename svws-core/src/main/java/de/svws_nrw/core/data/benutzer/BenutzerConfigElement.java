package de.svws_nrw.core.data.benutzer;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt ein Element der Konfiguration eines Benutzers
 */
@XmlRootElement
@Schema(description = "Ein Konfigurationselement.")
@TranspilerDTO
public class BenutzerConfigElement {

	/** Der Schlüssel des Konfigurationselements */
	@Schema(description = "der Schlüssel des Konfigurationselements.")
	public @NotNull String key = "";

	/** Der Wert des Konfigurationselements. */
	@Schema(description = "der Wert des Konfigurationselements.")
	public @NotNull String value = "";

}
