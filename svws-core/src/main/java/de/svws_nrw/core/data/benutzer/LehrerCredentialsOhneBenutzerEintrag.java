package de.svws_nrw.core.data.benutzer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die bereits vorhandenen Credentials eines Lehrers, dem noch kein
 * Lehrer-Benutzer zugeordnet ist.
 */
@XmlRootElement
@Schema(description = "Die bereits vorhandenen Credentials eines Lehrers ohne Lehrer-Benutzer.")
@TranspilerDTO
public class LehrerCredentialsOhneBenutzerEintrag {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerCredentialsOhneBenutzerEintrag() {
		// leer
	}

	/** Die ID des Lehrers */
	@Schema(description = "die ID des Lehrers", example = "42")
	public long idLehrer = -1;

	/** Die ID der vorhandenen Credentials */
	@Schema(description = "die ID der vorhandenen Credentials", example = "17")
	public long idCredentials = -1;

	/** Der Benutzername der vorhandenen Credentials */
	@Schema(description = "der Benutzername der vorhandenen Credentials", example = "KRZ")
	public @NotNull String benutzername = "";

}
