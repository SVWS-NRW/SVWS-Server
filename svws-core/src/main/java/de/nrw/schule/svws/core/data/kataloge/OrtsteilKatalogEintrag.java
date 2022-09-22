package de.nrw.schule.svws.core.data.kataloge;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für die Katalog der Ortsteile übergeben werden.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Ortsteile.")
@TranspilerDTO
public class OrtsteilKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="42")
	public long id;

	/** Die ID des zugehörigen Ortes. */
	@Schema(required = false, description = "die ID des zugehörigen Ortes", example="4711")
	public Long ort_id;

	/** Der Name des Ortsteils. */
	@Schema(required = true, description = "der Name des Ortsteils", example="Sieglar")
	public String ortsteil;
	
	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(required = true, description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example="1")
	public int sortierung;
	
	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example="true")
	public boolean istSichtbar;
	
	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example="true")
	public boolean istAenderbar;

}
