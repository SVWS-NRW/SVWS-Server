package de.svws_nrw.asd.data.schueler;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der möglichen Herkünfte
 * aus Typen von Bildungsgängen an Berufskollegs oder Weiterbildungskollegs
 * beim Wechsel zu einem Weiterbildungskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Herkünfte aus Typen von Bildungsgängen an Berufskollegs oder Weiterbildungskollegs.")
@TranspilerDTO
public class HerkunftBildungsgangTypKatalogEintrag extends CoreTypeDataNurSchulformenUndSchulgliederungen {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public HerkunftBildungsgangTypKatalogEintrag() {
		// leer
	}

}
