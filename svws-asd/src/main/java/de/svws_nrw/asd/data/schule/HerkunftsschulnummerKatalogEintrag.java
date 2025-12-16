package de.svws_nrw.asd.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Herkunftsschulnummern.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Herkunftsschulnummern.")
@TranspilerDTO
public class HerkunftsschulnummerKatalogEintrag extends CoreTypeDataNurSchulformenUndSchulgliederungen {

	/**
	 * Erstellt einen Herkunftsschulnummern-Eintrag mit Standardwerten
	 */
	public HerkunftsschulnummerKatalogEintrag() {
	}

}
