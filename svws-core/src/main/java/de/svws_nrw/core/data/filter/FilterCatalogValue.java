package de.svws_nrw.core.data.filter;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Response-DTO für einen auswählbaren Wert eines Katalogs. Alle Kataloge werden mit demselben Schema ausgeliefert.
 */
@XmlRootElement
@Schema(description = "Ein auswählbarer Wert eines Katalogs.")
@TranspilerDTO
public class FilterCatalogValue {

	/** Die ID des Katalog-Eintrags */
	@Schema(description = "Die ID des Katalog-Eintrags", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Bezeichnung des Katalog-Eintrags für die Anzeige */
	@Schema(description = "Die Bezeichnung des Katalog-Eintrags für die Anzeige", example = "53840 Troisdorf", accessMode = Schema.AccessMode.READ_ONLY)
	public String bezeichnung;

}
