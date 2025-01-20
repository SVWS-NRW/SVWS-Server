package de.svws_nrw.core.data.enm;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Eine Klasse mit einer Antwort auf eine Anfrage für das Ausführen einer
 * ENM-Server-Config-Operation im Server, welche angibt, ob die Operation erfolgreich war. ein
 * Log der Operation zurückgibt sowie die Server-Config selbst.
 */
@XmlRootElement(name = "ENMServerConfigPesponse")
@Schema(name = "ENM-Server-Config-Operation-Antwort", description = "die Antwort bei einer Operation mit dem Log der Operation sowie der Konfiguration.")
@TranspilerDTO
public class ENMConfigResponse {

	/** ID des zugehörigen Objektes. */
	@Schema(description = "ENM-Server-Konfiguration.")
	public ENMServerConfig config = null;

	/** Gibt an, ob die Operation erfolgreich war. */
	@Schema(description = "Gibt an, ob die Operation erfolgreich war.", example = "true")
	public boolean success = false;

	/** Das Log der Operation. */
	@ArraySchema(schema = @Schema(implementation = String.class))
	public @NotNull List<String> log = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMConfigResponse() {
		// leer
	}

}
