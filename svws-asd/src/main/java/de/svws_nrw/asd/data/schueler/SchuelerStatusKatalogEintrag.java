package de.svws_nrw.asd.data.schueler;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse stellt die Core-Types als Enumeration für
 * die Statuswerte bei Schülern zur Verfügung.
 */

@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Arten von SchuelerStatus.")
@TranspilerDTO
public class SchuelerStatusKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerStatusKatalogEintrag() {
		// leer
	}

}
