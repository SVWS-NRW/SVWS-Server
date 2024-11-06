package de.svws_nrw.asd.data.lehrer;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse spezifiziert den Core-DTO für einen Katalog-Eintrag im
 * Katalog der Leitungsfunktionen von Lehrern.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Leitungsfunktionen von Lehrern.")
@TranspilerDTO
public class LehrerLeitungsfunktionKatalogEintrag extends CoreTypeData {

	// Keine weiteren Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerLeitungsfunktionKatalogEintrag() {
		// leer
	}

}
