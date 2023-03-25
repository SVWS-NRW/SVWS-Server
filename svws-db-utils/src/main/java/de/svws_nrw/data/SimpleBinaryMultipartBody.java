package de.svws_nrw.data;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird als Multipart-Body eines Open-API-Aufrufs verwendet,
 * um Binärdaten zu übertragen.
 */
public class SimpleBinaryMultipartBody {

	/** Die Binärdaten */
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(type = "string", format = "binary", description = "database file")
	@FormParam("data")
    public byte[] data;

}
