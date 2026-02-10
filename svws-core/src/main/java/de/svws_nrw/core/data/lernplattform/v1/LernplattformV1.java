package de.svws_nrw.core.data.lernplattform.v1;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Datenstruktur für die Übermittlung/Export der ID und Bezeichnung einer Lernplattform..
 */
@XmlRootElement
@Schema(description = "Datenstruktur für die Übermittlung/Export der ID und Bezeichnung einer Lernplattform.")
@TranspilerDTO
public class LernplattformV1 {

	/** Die ID einer Lernplattform */
	@Schema(description = "Die ID einer Lernplattform", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Bezeichnung einer Lernplattform */
	@Schema(description = "Die Bezeichnung einer Lernplattform", example = "Lernplattform", accessMode = Schema.AccessMode.READ_ONLY)
	public String bezeichnung = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LernplattformV1() {
		// leer
	}

}
