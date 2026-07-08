package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieser DTO beinhaltet die Daten für den Katalog der Orte.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Orte")
@TranspilerDTO
public class OrteKatalogEintrag extends CoreTypeData {

	/** Die Postleitzahl des Ortes. */
	@Schema(description = "die Postleitzahl des Ortes", example = "52062")
	public String plz = null;

	/** Der Amtliche Gemeindeschlüssel (AGS). */
	@Schema(description = "der Amtliche Gemeindeschlüssel (AGS)", example = "05334002")
	public String ags = null;

	/** Der Ortsname. */
	@Schema(description = "der Ortsname", example = "Aachen")
	public String ort = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public OrteKatalogEintrag() {
		// leer
	}

}

