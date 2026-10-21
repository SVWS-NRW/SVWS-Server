package de.svws_nrw.asd.data.schule;
import de.svws_nrw.asd.data.CoreTypeDataNurSchulformen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;
/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog Talentschule.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog Talentschule.")
@TranspilerDTO
public class TalentschuleKatalogEintrag extends CoreTypeDataNurSchulformen {
	// keine weiteren Attribute vorhanden
	/**
	 * Leerer Standardkonstruktor.
	 */
	public TalentschuleKatalogEintrag() {
		// leer
	}
}
