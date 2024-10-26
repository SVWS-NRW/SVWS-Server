package de.svws_nrw.asd.data.lehrer;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gütligen Statistikwerte für den Katalog der LehrerEinsatzstati.
 */

@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Arten von LehrerEinsatzstatusKatalog.")
@TranspilerDTO
public class LehrerEinsatzstatusKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerEinsatzstatusKatalogEintrag() {
		// leer
	}

}
