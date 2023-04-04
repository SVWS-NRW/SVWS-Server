package de.svws_nrw.api.schema;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird als Multipart-Body eines Open-API-Aufrufs verwendet,
 * um eine Datenbank zu übertragen, welche nicht durch ein Kennwort geschützt ist.
 * Zusätzlich kann ein Benutzer und ein Kennwort für das Schema übergeben werden,
 * in welches die Quelldatenbank migriert bzw. importiert wird.
 */
public class DBMultipartBodyWithoutDBPassword {

	/** Die Quelldatenbank als Binärdatei. */
	@PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(type = "string", format = "binary", description = "database file")
	@FormParam("database")
    public byte[] database;

    /** Der Benutzername für das Schema, in welches migriert bzw. importiert werden soll. */
    @PartType(MediaType.TEXT_PLAIN)
	@Schema(implementation = String.class)
    @FormParam("schemaUsername")
    public String schemaUsername;

    /** Das Kennwort des Benutzer für das Schema, in welches migriert bzw. importiert werden soll. */
    @PartType(MediaType.TEXT_PLAIN)
	@Schema(implementation = String.class)
    @FormParam("schemaUserPassword")
    public String schemaUserPassword;

}
