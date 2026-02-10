package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für den schulspezifischen Katalog der Religionen
 * übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der schulspezifischen Religionen.")
@TranspilerDTO
public class ReligionStatistikGesamt {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Das Kürzel des Eintrages für die Statistik. */
	@Schema(description = "das Kürzel des Eintrages für die Statistik", example = "KR")
	public String kuerzel = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ReligionStatistikGesamt() {
		// leer
	}

}
