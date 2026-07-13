package de.svws_nrw.asd.data.schueler;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog AnrechnungsantragBKAZVO.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog AnrechnungsantragBKAZVO.")
@TranspilerDTO
public class AnrechnungsantragBKAZVOKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute vorhanden

	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public AnrechnungsantragBKAZVOKatalogEintrag() {
		// leer
	}

}
