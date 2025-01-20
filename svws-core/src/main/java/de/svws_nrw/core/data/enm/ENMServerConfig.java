package de.svws_nrw.core.data.enm;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.benutzer.BenutzerConfigElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zur Konfiguration
 * des ENM-Servers/Clients
 */
@XmlRootElement
@Schema(description = "Die Konfiguration des ENM-Servers.")
@TranspilerDTO
public class ENMServerConfig {

	/** Die Konfiguration, die dem Benutzer zugeordnet ist. */
	@Schema(description = "die Konfiguration, die dem Benutzer zugeordnet ist")
	public @NotNull List<BenutzerConfigElement> server = new ArrayList<>();

	/** Die globale Konfiguration, die auch für den Benutzer gilt. */
	@Schema(description = "die globale Konfiguration, die auch für den Benutzer gilt")
	public @NotNull List<BenutzerConfigElement> global = new ArrayList<>();
	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMServerConfig() {
		// leer
	}

}
