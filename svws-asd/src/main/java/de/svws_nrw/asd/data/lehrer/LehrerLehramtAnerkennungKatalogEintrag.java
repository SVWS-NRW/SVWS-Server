package de.svws_nrw.asd.data.lehrer;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Anerkennungsgründe für
 * ein Lehramt.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Anerkennungsgründe für Lehrämter.")
@TranspilerDTO
public class LehrerLehramtAnerkennungKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerLehramtAnerkennungKatalogEintrag() {
		// leer
	}

}
