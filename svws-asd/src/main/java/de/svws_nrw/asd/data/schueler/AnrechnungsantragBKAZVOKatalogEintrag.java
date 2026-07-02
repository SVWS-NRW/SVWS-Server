package de.svws_nrw.asd.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog AnrechnungsantragBKAZVO.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog AnrechnungsantragBKAZVO.")
@TranspilerDTO
public class AnrechnungsantragBKAZVOKatalogEintrag extends CoreTypeData {

	/** Eine zusätzliche längere Beschreibung der Einschulungsart. */
	@Schema(description = "die textuelle Beschreibung von AnrechnungsantragBKAZVO",
			example = "6 Monate")
	public @NotNull String beschreibung = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public AnrechnungsantragBKAZVOKatalogEintrag() {
		// leer
	}

}
