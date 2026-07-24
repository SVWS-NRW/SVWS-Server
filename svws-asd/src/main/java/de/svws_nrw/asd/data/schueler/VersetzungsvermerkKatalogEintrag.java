package de.svws_nrw.asd.data.schueler;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieser DTO beinhaltet die Daten für den Katalog der Versetzungsvermerke.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Versetzungsvermerke")
@TranspilerDTO
public class VersetzungsvermerkKatalogEintrag extends CoreTypeDataNurSchulformen {


	/** Der bisherige (deprecated) Schlüssel. */
	@Schema(description = "der bisherige (deprecated) Schlüssel", example = "0")
	public String schluesselDeprecated = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public VersetzungsvermerkKatalogEintrag() {
		// leer
	}

}
