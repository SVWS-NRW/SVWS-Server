package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten in Bezug auf den Noten-Katalog
 * für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Struktur von JSON-Daten in Bezug auf den Noten-Katalog für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMNote {

	/** Die ID der Note. */
	@Schema(description = "Die ID der Note.", example = "42")
	public int id;

	/** Die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-), ggf. auch ein Kürzel für PseudoNoten */
	@Schema(description = "Das eindeutige Kürzel der Note: Die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-) oder das Kürzel einer Pseudo-Note",
			example = "AT")
	public String kuerzel;

	/** Die Notenpunkte, die dieser Note ggf. zugeordnet sind */
	@Schema(description = "Die Notenpunkte, die dieser Note zugeordnet sind, sofern keine Pseudonote vorliegt", example = "15")
	public Integer notenpunkte;

	/** Die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus) */
	@Schema(description = "Die Note / Pseudonote in ausführlicher Textform ggf. mit Tendenz (plus/minus)", example = "befriedigend (minus)")
	public String text;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMNote() {
		// leer
	}

}
