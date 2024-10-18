package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Ein Core-DTO für den Katalog zur Erfassung der Dauer des Kindergartenbesuchs.
 */

@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Arten von Kindergartenbesuch.")
@TranspilerDTO
public class KindergartenbesuchKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute

}
