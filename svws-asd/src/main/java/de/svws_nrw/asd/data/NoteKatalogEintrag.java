package de.svws_nrw.asd.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Diese Klasse stellt die Core-Types als Enumeration für
 * die zulässigen Noteneinträge zur Verfügung.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Arten von SchuelerStatus.")
@TranspilerDTO
public class NoteKatalogEintrag extends CoreTypeData {

	/** Eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt */
	@Schema(description = "sortierung", example = "1")
	public @NotNull int sortierung = -1;

	/** Die Notenpunkte, die dieser Note zugeordnet sind */
	@Schema(description = "notenpunkte", example = "1")
	public Integer notenpunkte;

	/** Die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird. */
	@Schema(description = "textZeugnis", example = "sehr gut")
	public @NotNull String textZeugnis = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public NoteKatalogEintrag() {
		// leer
	}
}
