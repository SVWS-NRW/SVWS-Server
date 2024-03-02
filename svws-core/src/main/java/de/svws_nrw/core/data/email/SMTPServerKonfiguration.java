package de.svws_nrw.core.data.email;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, die Konfiguration des SMTP-Servers eines Email-Providers.
 */
@XmlRootElement
@Schema(description = "die Konfiguration für den SMTP-Server.")
@TranspilerDTO
public class SMTPServerKonfiguration {

	/** Die ID der Konfiguration. */
	@Schema(description = "die ID der Konfiguration", example = "1")
	public long id = -1;

	/** Der Hostname des SMTP-Servers. */
	@Schema(description = "der Hostname des SMTP-Servers", example = "smtp.example.com")
	public @NotNull String host = "";

	/** Die Port-Adresse des SMTP-Servers. */
	@Schema(description = "die Port-Adresse des SMTP-Servers", example = "25")
	public int port = 25;

	/** Gibt an, ob StartTLS für die SMTP-Verbindung genutzt wird oder nicht. */
	@Schema(description = "gibt an, ob StartTLS für die SMTP-Verbindung genutzt wird oder nicht", example = "true")
	public boolean useStartTLS = true;

	/** Gibt an, ob TLS für die SMTP-Verbindung genutzt wird oder nicht. Wird dies genutzt, so wird entweder ein Zertifikat im Key-Store des Servers benötigt oder es muss einem Host vertraut werden (siehe trustTLSHost). */
	@Schema(description = "gibt an, ob TLS für die SMTP-Verbindung genutzt wird oder nicht. Wird dies genutzt, so wird entweder ein Zertifikat im Key-Store des Servers benötigt oder es muss einem Host vertraut werden (siehe trustTLSHost)", example = "false")
	public boolean useTLS = false;

	/** Gibt an, falls nicht null, welchem Host - unabhängig von vorhandenen Zertifikaten - vertraut werden kann, '*' für jeden beliebigen Host. */
	@Schema(description = "gibt an, falls nicht null, welchem Host - unabhängig von vorhandenen Zertifikaten - vertraut werden kann, '*' für jeden beliebigen Host.", example = "*")
	public String trustTLSHost = null;

}
