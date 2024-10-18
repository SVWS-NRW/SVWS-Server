package de.svws_nrw.asd.data.jahrgang;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieser DTO beinhaltet die Daten für den Katalog der möglichen Einträge bei den
 * Jahrgängen
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Jahrgänge")
@TranspilerDTO
public class JahrgaengeKatalogEintrag extends CoreTypeDataNurSchulformen {

	// keine weiteren Attribute vorhanden

}
