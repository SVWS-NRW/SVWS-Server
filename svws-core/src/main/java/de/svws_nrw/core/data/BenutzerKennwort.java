package de.svws_nrw.core.data;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse stellt die Core-Types für
 * Benutzername und Passwort zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
@XmlRootElement
@Schema(description = "Der Benutzername und das Kennwort eines Benutzers.")
@TranspilerDTO
public class BenutzerKennwort {

	/** Der Benutzername. */
	@Schema(description = "Der Benutzername.", example = "vip")
	public String user;

	/** Das Kennwort des Benutzers. */
	@Schema(description = "Das Kennwort des Benutzers.", example = "geheim")
	public String password;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BenutzerKennwort() {
		// leer
	}

}
