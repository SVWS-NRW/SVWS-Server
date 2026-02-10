package de.svws_nrw.asd.data.lehrer;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt eine Fachrichtung eines Lehrers mit der übergebenen ID.
 */

@XmlRootElement
@Schema(description = "Eine Fachrichtung eines Lehrers.")
@TranspilerDTO
public class LehrerFachrichtungKatalogEintrag extends CoreTypeData {

	/** Die Bezeichner der zulässigen Lehrämter für die Fachrichtung */
	@Schema(description = "die Bezeichner der zulässigen Lehrämter für die Fachrichtung")
	public @NotNull List<String> lehraemter = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerFachrichtungKatalogEintrag() {
		// leer
	}

}
