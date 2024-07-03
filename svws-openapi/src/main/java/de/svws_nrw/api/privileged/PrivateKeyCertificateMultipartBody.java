package de.svws_nrw.api.privileged;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Der Multipart-Body eines Open-API-Aufrufs mit dem privaten Schlüssel und dem Zertifikat für
 * die TLS-Konfiguration des SVWS-Servers.
 */
public class PrivateKeyCertificateMultipartBody {

	/** Die Datenbank als Binärdatei. */
	@PartType(MediaType.APPLICATION_OCTET_STREAM)
	@Schema(type = "string", format = "binary", description = "Der private Schlüssel")
	@FormParam("key")
	public byte[] key;

	/** Die Datenbank als Binärdatei. */
	@PartType(MediaType.APPLICATION_OCTET_STREAM)
	@Schema(type = "string", format = "binary", description = "Das Zertifikat")
	@FormParam("certificate")
	public byte[] certificate;

}
