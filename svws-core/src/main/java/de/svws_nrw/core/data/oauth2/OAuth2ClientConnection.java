package de.svws_nrw.core.data.oauth2;

import com.sun.istack.NotNull;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt das Client Secret der Schule mit der übergebenen ID.
 */
@XmlRootElement
@Schema(description = "Ein OAuth2 Client Secret der Schule.")
@TranspilerDTO
public class OAuth2ClientConnection {

	/** Die ID des OAuth2 Client Secrets. */
	@Schema(description = "die ID des OAuth2 Client Secrets in der Datenbank", example = "126784")
	public long id = -1;

	/** Die URL des Auth-Servers. */
	@Schema(description = "Die URL des Auth-Servers.", example = "https://wenom2.svws-nrw.de")
	public @NotNull String authServer = "";

	/** Die Client-ID für diesen Auth-Server. */
	@Schema(description = "Die Client-ID für diesen Auth-Server.", example = "123abc")
	public @NotNull String clientID = "";

	/** Das Client-Secret für die Client ID für diesen Auth-Server. */
	@Schema(description = "Das Client-Secret für die Client ID für diesen Auth-Server.", example = "abc")
	public @NotNull String clientSecret = "";

	/** Das TLS-Zertifikat, welches von dem Auth-Server verwendet wird. */
	@Schema(description = "Das TLS-Zertifikat, welches von dem Auth-Server verwendet wird.")
	public String tlsCert = null;

	/** Gibt an, ob das TLS-Zertifikat von dem SVWS-Server über die Chain automatisch validiert werden kann. */
	@Schema(description = "gibt an, ob das TLS-Zertifikat von dem SVWS-Server über die Chain automatisch validiert werden kann.", example = "false")
	public boolean tlsCertIsKnown = false;

	/** Gibt an, ob dem TLS-Zertifikat von dem SVWS-Server vertraut wird oder nicht. */
	@Schema(description = "gibt an, ob dem TLS-Zertifikat von dem SVWS-Server vertraut wird oder nicht.", example = "false")
	public boolean tlsCertIsTrusted = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public OAuth2ClientConnection() {
		// leer
	}

}
