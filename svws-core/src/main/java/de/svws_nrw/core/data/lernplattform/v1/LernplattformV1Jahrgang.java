package de.svws_nrw.core.data.lernplattform.v1;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Datenstruktur eines Jahrgangs, innerhalb des Exports einer Lernplattform.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Datenstruktur eines Jahrgangs, innerhalb des Exports einer Lernplattform.")
@TranspilerDTO
public class LernplattformV1Jahrgang {

	/** Die ID des Jahrgangs aus der SVWS-DB (z. B. 12) */
	@Schema(description = "Die ID des Jahrgangs aus der SVWS-DB.", example = "12")
	public long id;

	/** Das Kürzel des Jahrgangs, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z. B. EFA) */
	@Schema(description = "Das Kürzel des Jahrgangs, wie es im Rahmen der amtlichen Schulstatistik verwendet wird.", example = "EFA")
	public String kuerzel;

	/** Das Kürzel der Jahrgangs, wie er im Rahmen der Schule benannt wird und angezeigt werden soll. (z. B. EF) */
	@Schema(description = "Das Kürzel der Jahrgangs, wie er im Rahmen der Schule benannt wird und angezeigt werden soll.", example = "EF")
	public String kuerzelAnzeige;

	/** Die Bezeichnung des Jahrgangs, welche bei der amtlichen Statistik verwendet wird. */
	@Schema(description = "Die Bezeichnung des Jahrgangs, welche bei der amtlichen Statistik verwendet wird.", example = "EF")
	public String bezeichnung;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LernplattformV1Jahrgang() {
		// leer
	}
}
