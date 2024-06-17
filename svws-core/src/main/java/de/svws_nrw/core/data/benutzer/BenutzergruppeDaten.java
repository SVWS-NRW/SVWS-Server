package de.svws_nrw.core.data.benutzer;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten einer Benutzergruppe mit der angegebenen ID.
 */
@XmlRootElement
@Schema(description = "Die Daten einer Benutzergruppe.")
@TranspilerDTO
public class BenutzergruppeDaten {

	/** Die ID der Benutzergruppe. */
	@Schema(description = "die ID der Benutzergruppe", example = "4711")
	public @NotNull long id = -1;

	/** Die Bezeichnung der Benutzergruppe. */
	@Schema(description = "die Bezeichnung der Benutzergruppe", example = "Administrator")
	public @NotNull String bezeichnung = "";

	/** Gibt an, ob es sich um eine Administrative Benutzergruppe handelt oder nicht. */
	@Schema(description = "gibt an, ob es sich um eine Administrative Benutzergruppe handelt oder nicht.", example = "true")
	public @NotNull boolean istAdmin = false;

	/** Die IDs der Kompetenzen, die speziell dieser Benutzergruppe zugeordnet sind. */
	@Schema(description = "die IDs der Kompetenzen, die speziell diesem Benutzer zugeordnet sind")
	public @NotNull List<@NotNull Long> kompetenzen = new ArrayList<>();

}
