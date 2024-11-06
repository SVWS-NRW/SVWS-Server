package de.svws_nrw.asd.data.klassen;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.schule.SchulformSchulgliederung;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Katalog der zulässigen Klassenarten und die Information für welche Schulformen
 * diese zulässig sind.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Klassenarten.")
@TranspilerDTO
public class KlassenartKatalogEintrag extends CoreTypeData {

	/** Die Informationen zu Schulformen und -gliederungen, wo die Klassenart zulässig ist. */
	@Schema(description = "die Informationen zu Schulformen und -gliederungen, wo die Klassenart zulässig ist.")
	public @NotNull List<SchulformSchulgliederung> zulaessig = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenartKatalogEintrag() {
		// leer
	}

}
