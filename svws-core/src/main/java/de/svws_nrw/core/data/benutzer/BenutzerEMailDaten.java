package de.svws_nrw.core.data.benutzer;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten für die EMail-Kommunikation durch einen Benutzer mit der angegebenen ID.
 */
@XmlRootElement
@Schema(description = "Die Daten für die EMail-Kommunikation per SMTP.")
@TranspilerDTO
public class BenutzerEMailDaten {

	/** Die ID des Benutzers. */
	@Schema(description = "die ID des Benutzers", example = "4711")
	public long id = -1;

	/** Der Name des Benutzers für das Versenden von E-Mails. */
	@Schema(description = "der Name des Benutzers für das Versenden von E-Mails", example = "Max Mustermann")
	public @NotNull String name = "";

	/** Die Mail-Adresse des Benutzers. */
	@Schema(description = "die Mail-Adresse des Benutzers", example = "max.mustermann@schule.de")
	public @NotNull String address = "";

	/** Der Anmeldename für den SMTP-Server. */
	@Schema(description = "der Anmeldename für den SMTP-Server", example = "mail4711")
	public @NotNull String usernameSMTP = "";

	/** Das AES-verschlüsselte SMTP-Kennwort des Benutzers. */
	@Schema(description = "das AES-verschlüsselte SMTP-Kennwort des Benutzers")
	public @NotNull String passwordSMTP = "";

	/** Die zu verwendende Signatur beim Versenden von E-Mails. */
	@Schema(description = "die zu verwendende Signatur beim Versenden von E-Mails")
	public @NotNull String signatur = "";

}
