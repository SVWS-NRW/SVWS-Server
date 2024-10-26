package de.svws_nrw.asd.data.fach;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Core-DTO für den Katalog der Sprachreferenzniveaus.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Arten von Sprachreferenzniveaus.")
@TranspilerDTO
public class SprachreferenzniveauKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SprachreferenzniveauKatalogEintrag() {
		// leer
	}

}
