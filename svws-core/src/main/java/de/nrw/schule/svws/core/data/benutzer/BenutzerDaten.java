package de.nrw.schule.svws.core.data.benutzer;

import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten eines Benutzer mit der angegebenen ID.  
 */
@XmlRootElement
@Schema(description="Die Daten eines Benutzers.")
@TranspilerDTO
public class BenutzerDaten {

	/** Die ID des Benutzers. */
	@Schema(required = true, description = "die ID des Benutzers", example="4711")
	public long id = -1;
	
	/** Der Typ des Benutzers. */
	@Schema(required = true, description = "der Typ des Benutzers", example="0")
	public @NotNull int typ = 0;
	
	/** die ID des Benutzers in der Typ-spezifischen-Tabelle (z.B. Lehrer-ID) */
	@Schema(required = true, description = "die ID des Benutzers in der Typ-spezifischen-Tabelle (z.B. Lehrer-ID)", example="4712")
	public @NotNull long typID = -1;

	/** Der Anzeigename des Benutzers. */
	@Schema(required = true, description = "der Anzeigename des Benutzers", example="Administrator")
	public @NotNull String anzeigename = "";
	
	/** Der Anmeldename des Benutzers */
	@Schema(required = true, description = "der Anmeldename des Benutzers", example="Admin")
	public @NotNull String name = "";
	
	/** Gibt an, ob es sich um einen Administrativen Benutzer handelt oder nicht. */
	@Schema(required = true, description = "gibt an, ob es sich um einen Administrativen Benutzer handelt oder nicht.", example="true")
	public @NotNull boolean istAdmin = false;
	
	/** Die ID der Credentials des Benutzers. */
	@Schema(required = true, description = "die ID der Credentials des Benutzers", example="42")
	public @NotNull long idCredentials = -1;
	
	/** Die Daten der Benutzergruppen, denen dieser Benutzer zugeordnet ist. */
	@Schema(required = true, description = "die Daten der Benutzergruppen, denen dieser Benutzer zugeordnet ist")
	public @NotNull List<@NotNull BenutzergruppeDaten> gruppen = new Vector<>();
	
	/** Die Kompetenzen, die speziell diesem Benutzer zugeordnet sind. */
	@Schema(required = true, description = "die Kompetenzen, die speziell diesem Benutzer zugeordnet sind")
	public @NotNull List<@NotNull BenutzerKompetenzKatalogEintrag> kompetenzen = new Vector<>();

	/** Die Kompetenzen, die entweder diesem Benutzer speziell oder über Gruppen zugeordnet sind. */
	@Schema(required = true, description = "die Kompetenzen, die entweder diesem Benutzer speziell oder über Gruppen zugeordnet sind")
	public @NotNull List<@NotNull BenutzerKompetenzKatalogEintrag> kompetenzenAlle = new Vector<>();

}
