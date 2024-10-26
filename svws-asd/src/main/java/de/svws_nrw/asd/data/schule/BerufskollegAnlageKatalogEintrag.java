package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Anlagen beim Berufskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Anlagen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegAnlageKatalogEintrag extends CoreTypeData {

	//keine weiteren Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BerufskollegAnlageKatalogEintrag() {
		// leer
	}

}
