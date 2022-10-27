package de.nrw.schule.svws.core.data.benutzer;

import java.util.List;
import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten eines Benutzer eines Schülers mit der angegebenen ID.  
 */
@XmlRootElement
@Schema(description="Die Stammdaten eines Schüler-Eintrags.")
@TranspilerDTO
public class BenutzergruppeDaten {

	/** Die ID der Benutzergruppe. */
	@Schema(required = true, description = "die ID der Benutzergruppe", example="4711")
	public long id = -1;

	/** Die Bezeichnung der Benutzergruppe. */
	@Schema(required = true, description = "die Bezeichnung der Benutzergruppe", example="Administrator")
	public @NotNull String bezeichnung = "";
	
	/** Gibt an, ob es sich um eine Administrative Benutzergruppe handelt oder nicht. */
	@Schema(required = true, description = "gibt an, ob es sich um eine Administrative Benutzergruppe handelt oder nicht.", example="true")
	public @NotNull boolean istAdmin = false;

	/** Die IDs der Kompetenzen, die speziell diesem Benutzer zugeordnet sind. */
	@Schema(required = true, description = "die IDs der Kompetenzen, die speziell diesem Benutzer zugeordnet sind")
	public @NotNull List<@NotNull Long> kompetenzen = new Vector<>();

}
