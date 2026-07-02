package de.svws_nrw.asd.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog FormBilingualerUnterricht.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog FormBilingualerUnterricht.")
@TranspilerDTO
public class FormBilingualerUnterrichtKatalogEintrag extends CoreTypeData {

	/** Eine zusätzliche längere Beschreibung der Einschulungsart. */
	@Schema(description = "die textuelle Beschreibung von FormBilingualerUnterricht",
			example = "nur innerhalb eines Bildungsgangs erteilt")
	public @NotNull String beschreibung = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public FormBilingualerUnterrichtKatalogEintrag() {
		// leer
	}

}
