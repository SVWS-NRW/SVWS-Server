package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Anrechnungen der Schüler des Herkunftssatzes (B-Schulen K86).
 */
@XmlRootElement
@Schema(description = "Die Anrechnungen der Schüler des Herkunftssatzes (B-Schulen K86).")
@TranspilerDTO
public class KlassenHerkunftAnrechungenStatistikExport {

	/** Die Schüler ohne Anrechung. */
	@Schema(description = "die Schüler ohne Anrechung.", example = "4")
	public int anrechnungOhne = 0;

	/** Die Schüler mit 6 Monate Anrechung. */
	@Schema(description = "die Schüler mit 6 Monate Anrechung.", example = "5")
	public int anrechnung6Monate = 0;

	/** Die Schüler mit 12 Monate Anrechung. */
	@Schema(description = "die Schüler mit 12 Monate Anrechung.", example = "6")
	public int anrechnung12Monate = 0;

	/** Die Schüler mit 18 Monate Anrechung. */
	@Schema(description = "die Schüler mit 18 Monate Anrechung.", example = "7")
	public int anrechnung18Monate = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenHerkunftAnrechungenStatistikExport() {
		// leer
	}

}
