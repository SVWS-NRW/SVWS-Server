package de.nrw.schule.svws.core.data.jahrgang;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Jahrgangs.  
 */
@XmlRootElement
@Schema(description="Die Daten eines Jahrgangs.")
@TranspilerDTO
public class JahrgangsDaten {

	/** Die ID des Jahrgangs. */
	@Schema(required = true, description = "die ID des Jahrgangs", example="4711")
	public long id;
	
	/** Das Kürzel des Jahrgangs. */
	@Schema(required = true, description = "das Kürzel des Jahrgangs", example="EF")
	public String kuerzel;

	/** Das dem Jahrgang zugeordnete Statistik-Kürzel. */
	@Schema(required = true, description = "das dem Jahrgang zugeordnete Statistik-Kürzel", example="EF")
	public String kuerzelStatistik;
	
	/** Der Name / die Bezeichnung des Jahrgangs. */
	@Schema(required = true, description = "der Name / die Bezeichnung des Jahrgangs", example="Einführungsphase")
	public String bezeichnung;

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(required = true, description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example="1")
	public int sortierung;

	/** Die ID der Schulgliederung, der der Eintrag zugeordnet ist. */
	@Schema(required = true, description = "die ID der Schulgliederung, der der Eintrag zugeordnet ist", example="***")
	public String kuerzelSchulgliederung;
	
	/** Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null */
	@Schema(required = false, description = "die ID des Folgejahrgangs, sofern einer definiert ist", example="4712")
	public Long idFolgejahrgang;
	
	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example="true")
	public boolean istSichtbar;

	// TODO Weitere Daten	
}
