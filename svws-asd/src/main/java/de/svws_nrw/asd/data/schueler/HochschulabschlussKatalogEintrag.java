package de.svws_nrw.asd.data.schueler;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieser DTO beinhaltet die Daten für den Katalog der Hochschulabschlüsse.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Hochschulabschlüsse")
@TranspilerDTO
public class HochschulabschlussKatalogEintrag extends CoreTypeData {



	/** Das DQR-Niveau des Hochschulabschlusses. */
	@Schema(description = "das DQR-Niveau des Hochschulabschlusses", example = "DQR_NIVEAU_6")
	public String dqrNiveau = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public HochschulabschlussKatalogEintrag() {
		// leer
	}

}
