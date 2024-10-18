package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der in der amtlichenn Schulstatistik
 * berücksichtigten Religionen / Konfessionen.
 */

@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Arten von Kindergartenbesuch.")
@TranspilerDTO
public class ReligionKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute

}
