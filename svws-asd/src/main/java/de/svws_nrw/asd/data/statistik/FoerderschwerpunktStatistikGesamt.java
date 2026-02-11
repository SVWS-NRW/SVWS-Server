package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten den schulspezifischen Förderschwerpunkt-Katalog übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem schulspezifischen Förderschwerpunkt-Katalog.")
@TranspilerDTO
public class FoerderschwerpunktStatistikGesamt {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id;

	/** Das Kürzel des Eintrags im Rahmen der amtlichen Schulstatisik. */
	@Schema(description = "das Kürzel des Eintrags im Rahmen der amtlichen Schulstatisik", example = "EZ")
	public @NotNull String kuerzelStatistik = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public FoerderschwerpunktStatistikGesamt() {
		// leer
	}

}
