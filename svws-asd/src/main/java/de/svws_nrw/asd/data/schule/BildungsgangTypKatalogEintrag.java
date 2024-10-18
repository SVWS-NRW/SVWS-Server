package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der (Schul-)Typen von Bildungsgängen beim Berufskolleg oder Weiterbildungskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Berufsschultypen von Bildungsgängen beim Berufskolleg oder Weiterbildungskolleg.")
@TranspilerDTO
public class BildungsgangTypKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute

}
