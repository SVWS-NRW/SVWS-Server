package de.svws_nrw.core.data.benutzer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt einen Listeneintrag einer Benutzergruppe.  
 */
@XmlRootElement
@Schema(description="ein Eintrag einer Benutzergruppe in der Benutzerliste.")
@TranspilerDTO
public class BenutzergruppeListeEintrag {

	/** Die ID der Benutzergruppe. */
	@Schema(required = true, description = "die ID der Benutzergruppe", example="4711")
	public long id = -1;

	/** Die Bezeichnung der Benutzergruppe. */
	@Schema(required = true, description = "die Bezeichnung der Benutzergruppe", example="Administrator")
	public @NotNull String bezeichnung = "";
	
	/** Gibt an, ob es sich um eine Administrative Benutzergruppe handelt oder nicht. */
	@Schema(required = true, description = "gibt an, ob es sich um eine Administrative Benutzergruppe handelt oder nicht.", example="true")
	public @NotNull boolean istAdmin = false;
	
}
