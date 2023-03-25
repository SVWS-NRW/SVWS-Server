package de.svws_nrw.core.data.fach;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für die Fächer übergeben werden.  
 */
@XmlRootElement
@Schema(description="ein Eintrag eines Faches in der Fächerliste.")
@TranspilerDTO
public class FaecherListeEintrag {

	/** Die ID des Faches. */
	@Schema(required = true, description = "die ID des Faches", example="4711")
	public long id;
	
	/** Das Kürzel des Faches. */
	@Schema(required = true, description = "das Kürzel des Faches", example="BI")
	public String kuerzel;

	/** Das dem Fach zugeordnete Statistik-Kürzel. */
	@Schema(required = true, description = "das dem Fach zugeordnete Statistik-Kürzel", example="BI")
	public String kuerzelStatistik;
	
	/** Der Name / die Bezeichnung des Faches. */
	@Schema(required = true, description = "der Name / die Bezeichnung des Faches", example="Biologie")
	public String bezeichnung;

	/** Die Sortierreihenfolge des Fächerlisten-Eintrags. */
	@Schema(required = true, description = "die Sortierreihenfolge des Fächerlisten-Eintrags", example="1")
	public int sortierung;
	
	/** Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht. */
	@Schema(required = true, description = "gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht", example="true")
	public boolean istOberstufenFach;	
	
	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example="true")
	public boolean istSichtbar;
	
	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example="true")
	public boolean istAenderbar;
	
	
}
