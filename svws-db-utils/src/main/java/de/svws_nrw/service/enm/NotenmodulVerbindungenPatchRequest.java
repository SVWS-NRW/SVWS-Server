package de.svws_nrw.service.enm;

import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Informationenen zur Aktualisierung einer existierenden Verbindung zu einem Webnotenmodul-Server
 */
@Schema(description = "Die Informationenen zur Aktualisierung einer existierenden Verbindung zu einem Webnotenmodul-Server")
public class NotenmodulVerbindungenPatchRequest {

	/** Die URL unter welcher der Server erreichbar ist */
	@Schema(description = "Die URL unter welcher der Server erreichbar ist.")
	@NotNull(message = "Es muss eine gültige Server-URL angegeben werden.")
	@Size(max = 255, message = "Die Server-URL darf maximal 255 Zeichen lang sein.")
	public JsonNullable<String> url = JsonNullable.undefined();

	/** Die Bezeichnung des Servers (optional). */
	@Schema(description = "Die Bezeichnung des Servers (optional).")
	@NotNull(message = "Es muss eine Bezeichnung angegeben werden.")
	public JsonNullable<String> bezeichnung = JsonNullable.undefined();

	/** Die Client-ID für die OAuth2-Authentifizierung beim ENM-Servers (Default "1") */
	@Schema(description = "Die Client-ID für die OAuth2-Authentifizierung beim ENM-Servers.")
	@NotBlank(message = "Es muss eine gültige Client-ID angegeben werden.")
	public JsonNullable<String> clientID = JsonNullable.undefined();

	/** Das Client-Secret zur Client ID für die Authentifizierung */
	@Schema(description = "Das Client-Secret zur Client ID für die Authentifizierung.")
	@NotNull(message = "Es muss eine Client-Secret angegeben werden.")
	public JsonNullable<String> clientSecret = JsonNullable.undefined();

	/** Gibt an, ob dem TLS-Zertifikat von dem SVWS-Server vertraut wird oder nicht */
	@Schema(description = "Gibt an, ob dem TLS-Zertifikat von dem SVWS-Server vertraut wird oder nicht")
	@NotNull(message = "Es muss angegeben werden, ob eine, Zertifikat vertraut wird oder nicht. null ist nicht erlaubt.")
	public JsonNullable<Boolean> serverTLSCertIsTrusted = JsonNullable.undefined();

}
