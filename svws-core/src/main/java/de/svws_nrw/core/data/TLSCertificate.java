package de.svws_nrw.core.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse beschreibt ein TLS-Zertifikate und wird bei der Kommunikation über die
 * Open-API-Schnittstelle verwendet.
 */
@XmlRootElement
@Schema(description = "Die Informationen zu einem TLS-Zertifikat.")
@TranspilerDTO
public class TLSCertificate {

	/** Die Version der Zertifikats (0 -> V1, 1 -> V2, 2 -> V3). */
	@Schema(description = "Die Version der Zertifikats (0 -> V1, 1 -> V2, 2 -> V3).", example = "2")
	public int version = 0;

	/** Der Typ des Zertifikats */
	@Schema(description = "Der Typ des Zertifikats")
	public @NotNull String type = "";

	/** Die Informationen zum Inhaber des Zertifikats */
	@Schema(description = "Die Informationen zum Inhaber des Zertifikats")
	public String subject;

	/** Die Informationen zum Aussteller des Zertifikats */
	@Schema(description = "Die Informationen zum Aussteller des Zertifikats")
	public String issuer;

	/** Die Seriennummer des Zertifikats */
	@Schema(description = "Die Seriennummer des Zertifikats", example = "00:CB:BF:21:9A:7F:99:BB:EE:37:46:76:A8:64:DF:42:7B")
	public String serialNumber;

	/** Der Signaturalgorithmus */
	@Schema(description = "Der Signaturalgorithmus", example = "RSA")
	public String signatureAlgorithm;

	/** Die OID des Signaturalgorithmus */
	@Schema(description = "Die OID des Signaturalgorithmus")
	public String signatureAlgorithmOID;

	/** Ggf. weitere Parameter für den Signaturalgorithmus */
	@Schema(description = "ggf. weitere Parameter für den Signaturalgorithmus")
	public String params = null;

	/** Die Signatur (Base64-kodiert) */
	@Schema(description = "Die Signatur (Base64-kodiert)")
	public @NotNull String signature = "";

	/** Der Algorithmus, der für den öffentlichen Schlüssel verwendet wurde */
	@Schema(description = "Der Algorithmus, der für den öffentlichen Schlüssel verwendet wurde", example = "RSA")
	public String keyAlgorithm;

	/** Das Format, in welchem der öffentliche Schlüssel vorliegt */
	@Schema(description = "Das Format, in welchem der öffentliche Schlüssel vorliegt")
	public String keyFormat;

	/** Der öffentliche Schlüssel (Base64-kodiert) */
	@Schema(description = "Der öffentliche Schlüssel (Base64-kodiert)")
	public @NotNull String key = "";

	/** Das Datum, ab dem das Zertifkat gültig ist (ISO 8601 format). */
	@Schema(description = "das Datum, ab dem das Zertifkat gültig ist (ISO 8601 format)", example = "2024-02-29")
	public String validSince;

	/** Das Datum, bis zu welchem Tag das Zertifkat gültig ist (ISO 8601 format). */
	@Schema(description = "das Datum, bis zu welchem Tag das Zertifkat gültig ist (ISO 8601 format)", example = "2028-02-29")
	public String validUntil;

	// TODO extensions

}
