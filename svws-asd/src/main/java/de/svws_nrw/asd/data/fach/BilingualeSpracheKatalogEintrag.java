package de.svws_nrw.asd.data.fach;


import de.svws_nrw.asd.data.CoreTypeDataNurSchulformen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Dieser DTO beinhaltet die Daten für den Katalog der möglichen Einträge bei den
 * bilingualen Sprachen
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Bilingualen Sprachfächer.")
@TranspilerDTO
public class BilingualeSpracheKatalogEintrag extends CoreTypeDataNurSchulformen {

	// keine weiteren Attribute vorhanden

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BilingualeSpracheKatalogEintrag() {
		// leer
	}

}
