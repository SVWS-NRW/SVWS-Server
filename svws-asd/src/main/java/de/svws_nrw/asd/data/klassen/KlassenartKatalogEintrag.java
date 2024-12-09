package de.svws_nrw.asd.data.klassen;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Katalog der zulässigen Klassenarten und die Information für welche Schulformen
 * diese zulässig sind.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Klassenarten.")
@TranspilerDTO
public class KlassenartKatalogEintrag extends CoreTypeDataNurSchulformenUndSchulgliederungen {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenartKatalogEintrag() {
		// leer
	}

}
