package de.svws_nrw.api.schema;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Zugangsdaten für die Migrationsprozesse übergeben werden.  
 */
public class MigrateBody {

	/** Gibt den Benutzernamen für die Quelldatenbank an. */
	@Schema(description = "Gibt den Benutzernamen für die Quelldatenbank an.", example="Admin")
    public String srcUsername; 

	/** Gibt das Kennwort für die Quelldatenbank an. */
	@Schema(description = "Gibt das Kennwort für die Quelldatenbank an.", example="Geheim")
    public String srcPassword;

	/**  Gibt den Ort der Quelldatenbank an. */
	@Schema(description = "Gibt den Ort der Quelldatenbank an.", example="localhost:4711")
    public String srcLocation;
    
	/** Gibt den Schema-Namen der Quelldatenbank. */
	@Schema(description = "Gibt den Schema-Namen der Quelldatenbank.", example="schild_nrw")
    public String srcSchema;

	/** Der Benutzername für den administrativen Zugang auf das neu zu erstellende Datenbank-Schema. */
	@Schema(description = "Der Benutzername für den administrativen Zugang auf das neu zu erstellende Datenbank-Schema.", example="svwsadmin")
    public String schemaUsername;
    
	/** Das Kennwort für den administrativen Zugang auf das neu zu erstellende Datenbank-Schema. */
	@Schema(description = "Das Kennwort für den administrativen Zugang auf das neu zu erstellende Datenbank-Schema.", example="StrengGeheim")
    public String schemaUserPassword;
        
}
