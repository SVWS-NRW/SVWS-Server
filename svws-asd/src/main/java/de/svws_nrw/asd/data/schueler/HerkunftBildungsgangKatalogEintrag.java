package de.svws_nrw.asd.data.schueler;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der möglichen Herkünfte
 * aus Bildungsgängen an Berufskollegs beim Wechsel zu einem Berufskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Herkünfte aus Bildungsgängen an Berufskollegs.")
@TranspilerDTO
public class HerkunftBildungsgangKatalogEintrag extends CoreTypeDataNurSchulformenUndSchulgliederungen {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public HerkunftBildungsgangKatalogEintrag() {
		// leer
	}

}
