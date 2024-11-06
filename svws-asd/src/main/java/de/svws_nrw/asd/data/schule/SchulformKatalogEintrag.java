package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Schulformen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Schulformen.")
@TranspilerDTO
public class SchulformKatalogEintrag extends CoreTypeData {

	/** Gibt an, ob eine Schule der Schulform eine gymnasiale Oberstufe haben kann oder nicht. */
	@Schema(description = "gibt an, ob eine Schule der Schulform eine gymnasiale Oberstufe haben kann oder nicht", example = "true")
	public boolean hatGymOb;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchulformKatalogEintrag() {
		// leer
	}

}
