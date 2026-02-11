package de.svws_nrw.core.data.enm;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.TLSCertificate;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie enthält die Informationen zu einer Verbindung zu einem Webnotenmodul-Server.
 */
@XmlRootElement
@Schema(description = "Die Informationen zu einer Verbindung zu einem Webnotenmodul-Server.")
@TranspilerDTO
public class ENMServerConnection {

	/** Die ID des OAuth2 Client Secrets. */
	@Schema(description = "die ID des OAuth2 ENM-Server Secrets in der Datenbank. -1 entspricht einer lokalen Umgebung", example = "126784")
	public long id = -1;

	/** Die Bezeichnung des Servers */
	@Schema(description = "die Bezeichnung des Servers", example = "Sekundarstufe 1 des Heinz Becker Gymnasiums Bexbach")
	public String bezeichnung = "";

	/** Die URL des Auth-Servers. */
	@Schema(description = "Die URL des Auth-Servers.", example = "https://wenom2.svws-nrw.de")
	public @NotNull String url = "";

	/** Die Client-ID für diesen Auth-Server. */
	@Schema(description = "Die Client-ID für diesen Auth-Server.", example = "123abc")
	public @NotNull String clientID = "";

	/** Das Client-Secret für die Client ID für diesen Auth-Server. */
	@Schema(description = "Das Client-Secret für die Client ID für diesen Auth-Server.", example = "abc")
	public @NotNull String clientSecret = "";

	/** Das TLS-Zertifikat, welches von dem Auth-Server verwendet wird. */
	@Schema(description = "Das TLS-Zertifikat, welches von dem Auth-Server verwendet wird.")
	public String serverTLSCert = null;

	/** Gibt an, ob das TLS-Zertifikat von dem SVWS-Server über die Chain automatisch validiert werden kann. */
	@Schema(description = "gibt an, ob das TLS-Zertifikat von dem SVWS-Server über die Chain automatisch validiert werden kann.", example = "false")
	public boolean serverTLSCertIsKnown = false;

	/** Gibt an, ob dem TLS-Zertifikat von dem SVWS-Server vertraut wird oder nicht. */
	@Schema(description = "gibt an, ob dem TLS-Zertifikat von dem SVWS-Server vertraut wird oder nicht.", example = "false")
	public boolean serverTLSCertIsTrusted = false;

	/** Die Liste mit den TLS-Zertifikaten der Zertifikatskette des TLS-Servers. */
	@ArraySchema(schema = @Schema(implementation = TLSCertificate.class,
			description = "Die Liste mit den TLS-Zertifikaten der Zertifikatskette des TLS-Servers."))
	public @NotNull List<TLSCertificate> serverTLSCertChain = new ArrayList<>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMServerConnection() {
		// leer
	}

}
