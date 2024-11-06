package de.svws_nrw.asd.data.kaoa;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA
 * gültigen Berufsfelder.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der KAoA-Berufsfelder.")
@TranspilerDTO
public class KAOABerufsfeldKatalogEintrag extends CoreTypeData {

	// Keine weiteren Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KAOABerufsfeldKatalogEintrag() {
		// leer
	}

}
