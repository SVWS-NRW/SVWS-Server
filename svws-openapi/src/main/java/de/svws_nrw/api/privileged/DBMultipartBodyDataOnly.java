package de.svws_nrw.api.privileged;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird als Multipart-Body eines Open-API-Aufrufs verwendet,
 * um eine Datenbank zu übertragen.
 */
public class DBMultipartBodyDataOnly {

	/** Die Datenbank als Binärdatei. */
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(type = "string", format = "binary", description = "database file")
	@FormParam("database")
    public byte[] database;

}
