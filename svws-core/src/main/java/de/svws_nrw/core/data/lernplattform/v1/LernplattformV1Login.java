package de.svws_nrw.core.data.lernplattform.v1;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Datenstruktur von Logindaten eines Schülers oder Lehrers, innerhalb des Exports einer Lernplattform.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Datenstruktur von Logindaten eines Schülers oder Lehrers, innerhalb des Exports einer Lernplattform.")
@TranspilerDTO
public class LernplattformV1Login {
	/** Der Benutzername für die Lernplattform. */
	@Schema(description = "Der Benutzername für die Lernplattform.", example = "max.mustermann")
	public String benutzername;

	/** Das Initialpasswort für die Lernplattform. */
	@Schema(description = "Das Initialpasswort für die Lernplattform.", example = "js32g23k")
	public String initialpasswort;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LernplattformV1Login() {
		// leer
	}

}
