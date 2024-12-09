package de.svws_nrw.core.data.benutzer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Credentials eines Lehrer-Benutzers.
 */
@XmlRootElement
@Schema(description = "Die Credentials eines Lehrer-Benutzers.")
@TranspilerDTO
public class BenutzerLehrerCredentials {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BenutzerLehrerCredentials() {
		// leer
	}

	/** Benutzername des Account-Credentials*/
	@Schema(description = "Benutzername des Account-Credentials", example = "42")
	public long idLehrer = -1;

	/** Benutzername des Account-Credentials*/
	@Schema(description = "Benutzername des Account-Credentials - Default ist das Lehrer-Kürzel", example = "KRZ")
	public @NotNull String benutzername = "";

	/** Passwort des Account-Credentials*/
	@Schema(description = "Benutzername des Account-Credentials", example = "StrengGeheim!")
	public @NotNull String password = "";

}
