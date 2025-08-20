package de.svws_nrw.asd.data.lehrer;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Vollzeit-Pflichtstunden-Sollwerte für Lehrer.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Lehrämter.")
@TranspilerDTO
public class LehrerPflichtstundensollVollzeitKatalogEintrag extends CoreTypeDataNurSchulformenUndSchulgliederungen {

	// keine weiteren Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerPflichtstundensollVollzeitKatalogEintrag() {
		// leer
	}

}
