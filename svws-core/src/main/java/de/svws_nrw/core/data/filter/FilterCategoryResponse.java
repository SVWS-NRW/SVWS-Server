package de.svws_nrw.core.data.filter;

import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Response-DTO für eine Filterkategorie. Eine Kategorie gruppiert beliebig viele Filterattribute.
 */
@XmlRootElement
@Schema(description = "Die Daten einer Filterkategorie.")
@TranspilerDTO
public class FilterCategoryResponse {

	/** Der Schlüssel der Kategorie */
	@Schema(description = "Der Schlüssel der Kategorie", example = "schueler.individualdaten", accessMode = Schema.AccessMode.READ_ONLY)
	public String key;

	/** Der Anzeigename der Kategorie */
	@Schema(description = "Der Anzeigename der Kategorie", example = "Individualdaten", accessMode = Schema.AccessMode.READ_ONLY)
	public String label;

	/** Die Sortierreihenfolge der Kategorie */
	@Schema(description = "Die Sortierreihenfolge der Kategorie", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public Integer order;

	/** Liste mit den Schlüsseln der zugeordneten Attribute */
	@Schema(description = "Liste mit Schlüsseln der zugeordneten Attribute", example = "schueler.status", accessMode = Schema.AccessMode.READ_ONLY)
	public List<String> attributes;

}
