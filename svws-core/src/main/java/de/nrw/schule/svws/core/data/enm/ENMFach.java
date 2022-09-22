package de.nrw.schule.svws.core.data.enm;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Fächern 
 * für das Externe-Noten-Modul (ENM). 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu den Fächern für das Externe-Noten-Modul (ENM).")
@TranspilerDTO
public class ENMFach {

	/** Die ID des Faches in der SVWS-DB. */
	@Schema(required = true, description = "Die ID des Faches in der SVWS-DB.", example="42")
	public long id;
	
	/** Das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. D) */
	@Schema(required = true, description = "Das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird.", example="D")
	public String kuerzel;

	/** Das Kürzel des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. D) */
	@Schema(required = true, description = "Das Kürzel des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll.", example="D")
	public String kuerzelAnzeige;

	/** Die Reihenfolge des Faches bei der Sortierung der Fächer. (z.B. 37) */
	@Schema(required = true, description = "Die Reihenfolge des Faches bei der Sortierung der Fächer.", example="37")
	public int sortierung;

	/** Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht. */
	@Schema(required = true, description = "Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht.", example="false")
	public boolean istFremdsprache;
	
}
