package de.svws_nrw.core.data.enm;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zur SMTP-Konfiguration des ENM-Servers
 */
@XmlRootElement
@Schema(description = "Die SMTP-Konfiguration des ENM-Servers.")
@TranspilerDTO
public class ENMServerConfigSMTP {

	/** Die Serveradresse des SMTP-Servers */
	@Schema(description = "Die Serveradresse des SMTP-Servers")
	public String host = null;

	/** Der Port des SMTP-Servers */
	@Schema(description = "Der Port des SMTP-Servers")
	public int port = 587;

	/** Der Benutzername für den SMTP-Login */
	@Schema(description = "Der Benutzername für den SMTP-Login")
	public String username = null;

	/** Das Passwort für den SMTP-Login */
	@Schema(description = "Das Passwort für den SMTP-Login")
	public String password = null;

	/** Verwendung TLS - true für TLS bzw. false für unverschlüsselt */
	@Schema(description = "Verwendung TLS - true für TLS bzw. false für unverschlüsselt")
	public boolean useTLS = true;

	/** Der Absender der E-Mail */
	@Schema(description = "Der Absender der E-Mail")
	public String fromEmail = null;

	/** Der Name des Absenders */
	@Schema(description = "Der Name des Absenders")
	public String fromName = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMServerConfigSMTP() {
		// leer
	}

}
