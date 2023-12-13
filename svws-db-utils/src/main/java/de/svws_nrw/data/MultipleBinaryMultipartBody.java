package de.svws_nrw.data;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird als Multipart-Body eines Open-API-Aufrufs verwendet,
 * um Binärdaten zu übertragen.
 */
public class MultipleBinaryMultipartBody {

	/** Die Binärdaten */
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @ArraySchema(schema = @Schema(type = "string", format = "binary", description = "database file"))
	@FormParam("data")
    public List<byte[]> data;

}
