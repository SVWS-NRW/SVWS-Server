package de.svws_nrw.core.data.benutzer;

import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Konfiguration eines Benutzers.
 */
@XmlRootElement
@Schema(description = "Die Konfiguration eines Benutzers.")
@TranspilerDTO
public class BenutzerConfig {

	/** Die Konfiguration, die dem Benutzer zugeordnet ist. */
	@Schema(description = "die Konfiguration, die dem Benutzer zugeordnet ist")
	public @NotNull Vector<@NotNull BenutzerConfigElement> user = new Vector<>();

	/** Die globale Konfiguration, die auch für den Benutzer gilt. */
	@Schema(description = "die globale Konfiguration, die auch für den Benutzer gilt")
	public @NotNull Vector<@NotNull BenutzerConfigElement> global = new Vector<>();

}
