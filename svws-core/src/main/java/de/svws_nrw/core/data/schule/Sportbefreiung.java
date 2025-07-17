package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Sportbefreiungen")
@TranspilerDTO
public class Sportbefreiung {

	/** Die ID der Sportbefreiung */
	@Schema(description = "Die ID der Sportbefreiung", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Bezeichnung der Sportbefreiung */
	@Schema(description = "Die Bezeichnung der Sportbefreiung", example = "Attest")
	public String bezeichnung;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung = 1;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = true;

	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example = "true")
	public boolean istAenderbar = true;

}
