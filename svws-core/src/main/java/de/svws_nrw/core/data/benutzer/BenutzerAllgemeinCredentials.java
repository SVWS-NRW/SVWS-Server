package de.svws_nrw.core.data.benutzer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Credentials eines allgemeinen Benutzers und dessen Anzeigenamen.
 */
@XmlRootElement
@Schema(description = "Die Credentials eines allgemeinen Benutzers und dessen Anzeigename.")
@TranspilerDTO
public class BenutzerAllgemeinCredentials {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BenutzerAllgemeinCredentials() {
		// leer
	}

	/** Benutzername des Account-Credentials*/
	@Schema(description = "Benutzername des Account-Credentials", example = "Max Musterman")
	public @NotNull String anzeigename = "";

	/** Benutzername des Account-Credentials*/
	@Schema(description = "Benutzername des Account-Credentials", example = "max.mustermann")
	public @NotNull String benutzername = "";

	/** Passwort des Account-Credentials*/
	@Schema(description = "Benutzername des Account-Credentials", example = "StrengGeheim!")
	public @NotNull String password = "";

}
