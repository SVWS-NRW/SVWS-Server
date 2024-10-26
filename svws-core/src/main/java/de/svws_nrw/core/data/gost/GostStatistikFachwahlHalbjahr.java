package de.svws_nrw.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Fachwahlinformationen eines Halbjahres eines Faches in einem Jahrgang der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Die Statistik zu den Fachwahlen eines Jahrgangs zu einem Fach der gymnasialen Oberstufe in Bezug auf ein Halbjahr.")
@TranspilerDTO
public class GostStatistikFachwahlHalbjahr {

	/** Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs. */
	@Schema(description = "die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs", example = "42")
	public int wahlenGKMuendlich = 0;

	/** Die Anzahl der Wahlen als schriftlicher Grundkurs. */
	@Schema(description = "die Anzahl der Wahlen als schriftlicher Grundkurs", example = "42")
	public int wahlenGKSchriftlich = 0;

	/** Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs. */
	@Schema(description = "die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs", example = "42")
	public int wahlenGK = 0;

	/** Die Anzahl der Wahlen als Zusatzkurs. */
	@Schema(description = "die Gesamtzahl der Wahlen als Zusatzkurs", example = "42")
	public int wahlenZK = 0;

	/** Die Gesamtzahl der Wahlen als Leistungskurs. */
	@Schema(description = "die Gesamtzahl der Wahlen als Leistungskurs", example = "42")
	public int wahlenLK = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostStatistikFachwahlHalbjahr() {
		// leer
	}

}
