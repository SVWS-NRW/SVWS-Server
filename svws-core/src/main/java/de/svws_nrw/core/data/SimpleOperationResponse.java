package de.svws_nrw.core.data;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Eine Klasse mit einer einfachen Antwort auf eine Anfrage für das ausführen einer
 * Operation im Server, welche angibt, ob die Operation erfolgreich war und ein
 * Log der Operation zurückgibt.
 */
@XmlRootElement(name = "SimpleOperationResponse")
@Schema(name = "Einfache-Operation-Antwort", description = "die Antwort bei einer Operation mit dem Log der Operation.")
@TranspilerDTO
public class SimpleOperationResponse {

	/** ID des zugehörigen Objektes. */
	@Schema(description = "ID des zugehörigen Objektes.", example = "11")
	public Long id = null;

	/** Gibt an, ob die Operation erfolgreich war. */
	@Schema(description = "Gibt an, ob die Operation erfolgreich war.", example = "true")
	public boolean success = false;

	/** Das Log der Operation. */
	@ArraySchema(schema = @Schema(implementation = String.class))
	public @NotNull List<String> log = new ArrayList<>();

}
