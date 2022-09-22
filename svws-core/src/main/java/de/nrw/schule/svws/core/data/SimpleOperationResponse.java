package de.nrw.schule.svws.core.data;

import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Eine Klasse mit einer einfachen Antwort auf eine Anfrage für das ausführen einer
 * Operation im Server, welche angibt, ob die Operation erfolgreich war und ein
 * Log der Operation zurückgibt.  
 */
@XmlRootElement(name = "SimpleOperationResponse")
@Schema(name="Einfache-Operation-Antwort", description="die Antwort bei einer Operation mit dem Log der Operation.")
@TranspilerDTO
public class SimpleOperationResponse {
	
	/** Gibt an, ob die Operation erfolgreich war. */
	@Schema(required = false, description = "Gibt an, ob die Operation erfolgreich war.", example="true")
	public boolean success = false;
	
	/** Das Log der Operation. */
    @ArraySchema(schema = @Schema(implementation = String.class))
	public @NotNull List<String> log = new Vector<>();

}
