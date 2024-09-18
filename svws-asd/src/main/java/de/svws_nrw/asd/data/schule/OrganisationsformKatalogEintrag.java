package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse stellt die Daten für den Katalog der Organisationsformen
 * bei allgemeinbildenden Schulen, bei berufsbildenden Schule und beim
 * Weiterbildungskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Organisationsformen.")
@TranspilerDTO
public class OrganisationsformKatalogEintrag extends CoreTypeDataNurSchulformen {

	// keine weiteren Attribute vorhanden

}
