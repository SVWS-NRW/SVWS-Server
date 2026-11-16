package de.svws_nrw.core.data.filter;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Response-DTO für ein Filterattribut. Der Typ bestimmt, woher die auswählbaren Werte des Attributes stammen.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Filterattributes.")
@TranspilerDTO
public class FilterAttributeResponse {

	/** Der Schlüssel des Attributes */
	@Schema(description = "Der Schlüssel des Attributes", example = "schueler.status", accessMode = Schema.AccessMode.READ_ONLY)
	public String key;

	/** Der Anzeigename des Attributes */
	@Schema(description = "Der Anzeigename des Attributes", example = "Status", accessMode = Schema.AccessMode.READ_ONLY)
	public String label;

	/** Der Schlüssel der Kategorie */
	@Schema(description = "Der Schlüssel der Kategorie", example = "schueler.individualdaten", accessMode = Schema.AccessMode.READ_ONLY)
	public String keyCategory;

	/** Der Typ des Attributes */
	@Schema(description = "Der Typ des Attributes: string, number, date, boolean, enum, coretype oder catalog", example = "coretype",
			accessMode = Schema.AccessMode.READ_ONLY)
	public String type;

	/** Der Name des Enums, CoreTypes oder Katalogs, aus dem die Werte stammen */
	@Schema(description = "Der Name des Enums, CoreTypes oder Katalogs, aus dem die Werte stammen. Bei Attributen ohne solche Quelle ist der Wert null.",
			example = "SchuelerStatus", accessMode = Schema.AccessMode.READ_ONLY, nullable = true)
	public String source;

}
