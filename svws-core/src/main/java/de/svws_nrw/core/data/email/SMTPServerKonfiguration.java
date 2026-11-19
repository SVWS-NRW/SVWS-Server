package de.svws_nrw.core.data.email;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
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

	/** Gibt an, ob TLS für die SMTP-Verbindung genutzt wird oder nicht. Das Zertifikat des SMTP-Servers muss dann vertrauenswürdig sein oder zum Fingerabdruck in zertifikatFingerabdruck passen. */
	@Schema(description = "gibt an, ob TLS für die SMTP-Verbindung genutzt wird oder nicht. Das Zertifikat des SMTP-Servers muss dann vertrauenswürdig sein oder zum Fingerabdruck in zertifikatFingerabdruck passen.",
			example = "false")
	public boolean useTLS = false;

	/** Gibt an, falls nicht null, welchem Host - unabhängig von vorhandenen Zertifikaten - vertraut werden kann, '*' für jeden beliebigen Host. Die Angabe nutzen ausschließlich externe Programme, der SVWS-Server wertet sie nicht aus. */
	@Schema(description = "gibt an, falls nicht null, welchem Host - unabhängig von vorhandenen Zertifikaten - vertraut werden kann, '*' für jeden beliebigen Host. Die Angabe nutzen ausschließlich externe Programme, der SVWS-Server wertet sie nicht aus.",
			example = "*")
	public String trustTLSHost = null;

	/** Der SHA-256-Fingerabdruck eines Zertifikats des SMTP-Servers oder null. Ist er gesetzt, vertraut der SVWS-Server nur noch diesem einen Zertifikat, zum Beispiel auch einem selbst erstellten. */
	@Schema(description = "der SHA-256-Fingerabdruck eines Zertifikats des SMTP-Servers oder null. Ist er gesetzt, vertraut der SVWS-Server nur noch diesem einen Zertifikat, zum Beispiel auch einem selbst erstellten.",
			example = "3A:F2:9C:0E:51:7B:D4:88:26:6F:A1:C3:90:5D:E7:12:4B:B8:0F:6C:D9:73:A5:2E:18:C4:F0:9B:63:7D:E2:45")
	public String zertifikatFingerabdruck = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SMTPServerKonfiguration() {
		// leer
	}

}
