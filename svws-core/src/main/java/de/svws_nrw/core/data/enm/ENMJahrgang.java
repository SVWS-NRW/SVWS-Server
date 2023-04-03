package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Jahrgängen der
 * Schüler für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Struktur von JSON-Daten zu den Jahrgängen der Schüler für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMJahrgang {

	/** Die ID des Jahrgangs aus der SVWS-DB (z.B. 16) */
	@Schema(description = "Die ID des Jahrgangs aus der SVWS-DB.", example = "12")
	public long id;

	/** Das Kürzel des Jahrgangs, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. EF) */
	@Schema(description = "Das Kürzel des Jahrgangs, wie es im Rahmen der amtlichen Schulstatistik verwendet wird.", example = "EF")
	public String kuerzel;

	/** Das Kürzel des Jahrgangs, wie er im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. EF) */
	@Schema(description = "Das Kürzel des Jahrgangs, wie er im Rahmen der Schule benannt wird und angezeigt werden soll.", example = "EF")
	public String kuerzelAnzeige;

	/** Die textuelle Bezeichnung des Jahrgangs. (z.B. Einführungsphase) */
	@Schema(description = "Die textuelle Bezeichnung des Jahrgangs.", example = "Einführungsphase")
	public String beschreibung;

	/** Die Stufe des Jahrgangs. (z.B. PR, SI, nur Berufskolleg: SII, Berufskolleg Anlage D und GOSt: SII-1, SII-2, SII-3) */
	@Schema(description = "Die Stufe des Jahrgangs. (z.B. PR, SI, nur Berufskolleg: SII, Berufskolleg Anlage D und GOSt: SII-1, SII-2, SII-3).", example = "SII-1")
	public String stufe;

	/** Die Reihenfolge des Jahrgangs bei der Sortierung der Jahrgänge. (z.B. 8) */
	@Schema(description = "Die Reihenfolge des Jahrgangs bei der Sortierung der Jahrgänge.", example = "20")
	public int sortierung;

}
