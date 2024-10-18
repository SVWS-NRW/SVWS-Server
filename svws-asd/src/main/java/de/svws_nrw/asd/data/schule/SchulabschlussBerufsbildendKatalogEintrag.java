package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Arten von allgemeinbildenden Schulabschlüsse.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Arten von berufsbildenden Schulabschlüssen.")
@TranspilerDTO
public class SchulabschlussBerufsbildendKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute

}
