package de.svws_nrw.asd.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Berufsebenen beim Berufskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Berufsebenen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegBerufsebeneKatalogEintrag extends CoreTypeData {

	/** Die Berufsebene. */
	@Schema(description = "die Berufsebene", example = "1")
	public int ebene;


	/**
	 * Erstellt einen Berufsebene-Eintrag mit Standardwerten
	 */
	public BerufskollegBerufsebeneKatalogEintrag() {
	}

}
