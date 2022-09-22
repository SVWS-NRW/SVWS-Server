package de.nrw.schule.svws.api.schema;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird als Multipart-Body eines Open-API-Aufrufs verwendet,
 * um eine Datenbank mit einem zugehörigen Kennwort zu übertragen.
 * Es wird kein Benutzer mit Kennwort für das Schema übergeben, 
 * in welches die Quelldatenbank migriert bzw. importiert wird. Stattdessen
 * werden Default-Werte gesetzt. Diese Variante sollte nur für Testdatenbanken 
 * verwendet werden!  
 */
public class DBMultipartBodyDefaultSchema {

	
	/** Die Quelldatenbank als Binärdatei. */
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(type = "string", format = "binary", description = "database file")
	@FormParam("database")
    public byte[] database;
	
    /** Das Datenbankkennwort für die Quelldatenbank */
    @PartType(MediaType.TEXT_PLAIN)
	@Schema(implementation = String.class) 
    @FormParam("databasePassword")
    public String databasePassword;

}
