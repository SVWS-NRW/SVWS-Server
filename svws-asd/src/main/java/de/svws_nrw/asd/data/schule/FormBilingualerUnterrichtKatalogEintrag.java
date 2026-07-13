package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog FormBilingualerUnterricht.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog FormBilingualerUnterricht.")
@TranspilerDTO
public class FormBilingualerUnterrichtKatalogEintrag extends CoreTypeData {

	// keine weiteren Attribute vorhanden

	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public FormBilingualerUnterrichtKatalogEintrag() {
		// leer
	}

}
