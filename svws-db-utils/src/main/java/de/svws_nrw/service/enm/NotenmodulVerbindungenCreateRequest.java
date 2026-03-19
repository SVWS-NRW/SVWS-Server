package de.svws_nrw.service.enm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Informationenen zur Erstellung einer Verbindung zu einem Webnotenmodul-Server.
 */
@Schema(description = "Die Informationenen zur Erstellung einer Verbindung zu einem Webnotenmodul-Server")
public class NotenmodulVerbindungenCreateRequest {

	/** Die URL unter welcher der Server erreichbar ist */
	@Schema(description = "Die URL unter welcher der Server erreichbar ist", example = "https://enm.schule.nrw.de")
	@NotNull(message = "Es muss eine gültige Server-URL angegeben werden.")
	@Size(max = 255, message = "Die Server-URL darf maximal 255 Zeichen lang sein.")
	public String url;

	/** Die Bezeichnung des Servers (optional). */
	@Schema(description = "Die Bezeichnung des Servers (optional).", example = "Externer Server 1")
	@NotNull(message = "Es muss eine Bezeichnung angegeben werden.")
	public String bezeichnung;

	/** Die Client-ID für die OAuth2-Authentifizierung beim ENM-Servers (Default "1") */
	@Schema(description = "Die Client-ID für die OAuth2-Authentifizierung beim ENM-Servers", example = "1")
	@NotBlank(message = "Es muss eine gültige Client-ID angegeben werden.")
	public String clientID = "1";

	/** Das Client-Secret zur Client ID für die Authentifizierung */
	@Schema(description = "Das Client-Secret zur Client ID für die Authentifizierung")
	@NotNull(message = "Es muss eine Client-Secret angegeben werden.")
	public String clientSecret;

}
