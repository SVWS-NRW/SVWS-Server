package de.svws_nrw.core.data.filter;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Response-DTO für ein Filterattribut, dessen Werte aus einem Katalog stammen.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Katalog Filterattributes.")
@TranspilerDTO
public class CatalogFilterAttributeResponse {

	/** Der Schlüssel des Attributes */
	@Schema(description = "Der Schlüssel des Attributes", example = "schueler.wohnort", accessMode = Schema.AccessMode.READ_ONLY)
	public String key;

	/** Der Anzeigename des Attributes */
	@Schema(description = "Der Anzeigename des Attributes", example = "Ort", accessMode = Schema.AccessMode.READ_ONLY)
	public String label;

	/** Der Name des Katalogs */
	@Schema(description = "Der Name des Katalogs", example = "Orte", accessMode = Schema.AccessMode.READ_ONLY)
	public String catalogName;

	/** Der Schlüssel der Kategorie */
	@Schema(description = "Der Schlüssel der Kategorie", example = "schueler.individualdaten", accessMode = Schema.AccessMode.READ_ONLY)
	public String keyCategory;

}
