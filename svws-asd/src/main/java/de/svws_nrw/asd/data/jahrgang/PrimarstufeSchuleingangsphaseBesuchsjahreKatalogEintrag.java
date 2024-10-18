package de.svws_nrw.asd.data.jahrgang;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieser DTO beinhaltet die Daten für den Katalog der möglichen Einträge bei den
 * Besuchsjahren der Schuleingangsphase von Schülern der Primarstufe.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Besuchsjahre der Schuleingangsphase von Schülern der Primarstufe.")
@TranspilerDTO
public class PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag extends CoreTypeData {

	// Keine weiteren Attribute

}
