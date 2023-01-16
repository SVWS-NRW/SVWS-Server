package de.nrw.schule.svws.core.data.benutzer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten eines internen Account-Credentials mit der angegebenen ID.  
 */
@XmlRootElement
@Schema(description="Die Daten eines Account-Credentials.")
@TranspilerDTO
public class Credentials {

    
    /** Benutzername des Account-Credentials*/
    @Schema(required = true, description = "Benutzername des Account-Credentials", example="Max")
    public @NotNull String benutzername = "";
    
    /** Passwort des Account-Credentials*/
    @Schema(required = true, description = "Benutzername des Account-Credentials", example="Max")
    public @NotNull String password = "";
   

}
