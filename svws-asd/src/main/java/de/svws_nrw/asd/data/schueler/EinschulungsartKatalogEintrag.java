package de.svws_nrw.asd.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der Einschulungsarten.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Einschulungsarten.")
@TranspilerDTO
public class EinschulungsartKatalogEintrag extends CoreTypeData {

	/** Eine zusätzliche längere Beschreibung der Einschulungsart. */
	@Schema(description = "die textuelle Beschreibung der Einschulungsart",
			example = "Kinder, die bis zum gültigen Einschulungsstichtag das 6. Lebensjahr vollendet haben")
	public @NotNull String beschreibung = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public EinschulungsartKatalogEintrag() {
	}

}
