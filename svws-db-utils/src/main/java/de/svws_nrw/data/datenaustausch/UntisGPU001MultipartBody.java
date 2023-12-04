package de.svws_nrw.data.datenaustausch;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import de.svws_nrw.core.data.stundenplan.StundenplanListeEintragMinimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird als Multipart-Body eines Open-API-Aufrufs verwendet,
 * um Binärdaten zu übertragen.
 */
public class UntisGPU001MultipartBody {

	/** die Minimal-Informationen zu dem Stundenplan */
    @PartType(MediaType.TEXT_PLAIN)
    @Schema(description = "die Minimal-Informationen zu dem Stundenplan", implementation = StundenplanListeEintragMinimal.class)
	@FormParam("entry")
    public String entry;

	/** Die Textdatei */
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(type = "string", format = "binary", description = "file")
	@FormParam("data")
    public byte[] data;

}
