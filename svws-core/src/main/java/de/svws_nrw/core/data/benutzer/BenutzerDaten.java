package de.svws_nrw.core.data.benutzer;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten eines Benutzer mit der angegebenen ID.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Benutzers.")
@TranspilerDTO
public class BenutzerDaten {

	/** Die ID des Benutzers. */
	@Schema(description = "die ID des Benutzers", example = "4711")
	public long id = -1;

	/** Der Typ des Benutzers. */
	@Schema(description = "der Typ des Benutzers", example = "0")
	public @NotNull int typ = 0;

	/** die ID des Benutzers in der Typ-spezifischen-Tabelle (z.B. Lehrer-ID) */
	@Schema(description = "die ID des Benutzers in der Typ-spezifischen-Tabelle (z.B. Lehrer-ID)", example = "4712")
	public @NotNull long typID = -1;

	/** Der Anzeigename des Benutzers. */
	@Schema(description = "der Anzeigename des Benutzers", example = "Administrator")
	public @NotNull String anzeigename = "";

	/** Der Anmeldename des Benutzers */
	@Schema(description = "der Anmeldename des Benutzers", example = "Admin")
	public @NotNull String name = "";

	/** Gibt an, ob es sich um einen Administrativen Benutzer handelt oder nicht. */
	@Schema(description = "gibt an, ob es sich um einen Administrativen Benutzer handelt oder nicht.", example = "true")
	public @NotNull boolean istAdmin = false;

	/** Die ID der Credentials des Benutzers. */
	@Schema(description = "die ID der Credentials des Benutzers", example = "42")
	public @NotNull long idCredentials = -1;

	/** Die Daten der Benutzergruppen, denen dieser Benutzer zugeordnet ist. */
	@Schema(description = "die Daten der Benutzergruppen, denen dieser Benutzer zugeordnet ist")
	public @NotNull List<@NotNull BenutzergruppeDaten> gruppen = new ArrayList<>();

	/** Die Kompetenzen, die speziell diesem Benutzer zugeordnet sind. */
	@Schema(description = "die Kompetenzen, die speziell diesem Benutzer zugeordnet sind")
	public @NotNull ArrayList<@NotNull Long> kompetenzen = new ArrayList<>();


}
