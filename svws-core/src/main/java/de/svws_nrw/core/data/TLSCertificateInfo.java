package de.svws_nrw.core.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse stellt Informationen für das Erstellen eines TLS-Zertifikates bereit.
 */
@XmlRootElement
@Schema(description = "Die Informationen für das Erstellen eines TLS-Zertifikats.")
@TranspilerDTO
public class TLSCertificateInfo {

	/** Der Distinguished Name (DN) */
	@Schema(description = "Der Distinguished Name (DN)")
	public @NotNull String dn = "";

	/** Die Subject Alternative Name (SAN)-Einträge (DNS oder IP) */
	@ArraySchema(schema = @Schema(implementation = String.class, description = "Die Subject Alternative Name (SAN)-Einträge (DNS oder IP)"))
	public @NotNull List<String> sans = new ArrayList<>();

}
