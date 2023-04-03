package de.svws_nrw.core.data.enm;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
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
	@Schema(description = "Die ID des Faches in der SVWS-DB.", example="42")
	public long id;
	
	/** Das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. D) */
	@Schema(description = "Das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird.", example="D")
	public @NotNull String kuerzel = "";

	/** Das Kürzel des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. D) */
	@Schema(description = "Das Kürzel des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll.", example="D")
	public @NotNull String kuerzelAnzeige = "";

	/** Die Reihenfolge des Faches bei der Sortierung der Fächer. (z.B. 37) */
	@Schema(description = "Die Reihenfolge des Faches bei der Sortierung der Fächer.", example="37")
	public int sortierung;

	/** Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht. */
	@Schema(description = "Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht.", example="false")
	public boolean istFremdsprache;
	
}
