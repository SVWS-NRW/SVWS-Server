package de.svws_nrw.asd.data.schueler;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Ein Core-DTO für Katalog-Einträge zu der Übergangsempfehlung eines Schüler nach der 4. Klasse in die
 * 5. Klasse der Sekundarstufe I
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Übergangsempfehlungen.")
@TranspilerDTO
public class UebergangsempfehlungKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UebergangsempfehlungKatalogEintrag() {
		// leer
	}

}
