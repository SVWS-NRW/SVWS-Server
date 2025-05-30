package de.svws_nrw.asd.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Verkehrssprachen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Verkehrssprachen.")
@TranspilerDTO
public class VerkehrsspracheKatalogEintrag extends CoreTypeData {

	/** Der dreistellige ISO 639-2 code */
	@Schema(description = "der dreistellige ISO 639-2 code", example = "deu")
	public @NotNull String iso3 = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public VerkehrsspracheKatalogEintrag() {
		// leer
	}

}
