package de.svws_nrw.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Diese Klasse beschreibt die Konfiguration eines Datenbank-Schemas
 * innerhalb einer SVWS-Konfiguration.
 */
class SVWSKonfigurationSchemaDTO {

	/** Der Name des Datenbank-Schemas für den SVWS-Benutzer */
	@JsonProperty("name")
	String name;

	/** Gibt an, ob der SVWS-Anmeldename und das zugehörige Kennwort auch für die Datenbankverbindung genutzt
	 * wird oder nicht. Erfordert das Anlegen eines Datenbankbenutzers für jeden SVWS-Benutzer */
	@JsonProperty("svwslogin")
	Boolean svwslogin;

	/** Der Benutzername für die Verbindung zur Datenbank, falls nicht der SVWS-Benutzername dafür genutzt wird - siehe svwslogin */
	@JsonProperty("username")
	String username;

	/** Das Kennwort für die Verbindung zur Datenbank, falls nicht das Kennwort des SVWS-Benutzer dafür genutzt wird - siehe svwslogin */
	@JsonProperty("password")
	String password;

}
