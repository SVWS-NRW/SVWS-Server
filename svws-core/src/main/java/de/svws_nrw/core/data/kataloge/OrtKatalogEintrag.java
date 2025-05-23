package de.svws_nrw.core.data.kataloge;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für die Katalog der Orte übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Orte.")
@TranspilerDTO
public class OrtKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id;

	/** Die Postleitzahl. */
	@Schema(description = "die Postleitzahl", example = "53840")
	public String plz;

	/** Der Name des Ortes. */
	@Schema(description = "der Name des Ortes", example = "Troisdorf")
	public String ortsname;

	/** Der Name des Kreises. */
	@Schema(description = "der Name des Kreises", example = "Rhein-Sieg-Kreis")
	public String kreis;

	/** Das Kürzel des Bundeslandes.  */
	@Schema(description = "das Kürzel des Bundeslandes", example = "NW")
	public String kuerzelBundesland;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example = "true")
	public boolean istAenderbar;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public OrtKatalogEintrag() {
		// leer
	}

}
