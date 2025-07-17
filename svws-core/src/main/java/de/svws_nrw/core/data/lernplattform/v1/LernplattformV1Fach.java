package de.svws_nrw.core.data.lernplattform.v1;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Datenstruktur zu einem Fach, innerhalb des Exports einer Lernplattform.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Datenstruktur eines Fachs, innerhalb des Exports einer Lernplattform.")
@TranspilerDTO
public class LernplattformV1Fach {

	/** Die ID des Faches in der SVWS-DB. */
	@Schema(description = "Die ID des Faches in der SVWS-DB.", example = "42")
	public long id;

	/** Das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. D) */
	@Schema(description = "Das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird.", example = "D")
	public String kuerzel = "";

	/** Die Kürzelanzeige des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. D) */
	@Schema(description = "Das Kürzel des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll.", example = "D")
	public String kuerzelAnzeige = "";

	/** Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht. */
	@Schema(description = "Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht.", example = "false")
	public boolean istFremdsprache;
	/**
	 * Leerer Standardkonstruktor.
	 */
	public LernplattformV1Fach() {
		// leer
	}

}
