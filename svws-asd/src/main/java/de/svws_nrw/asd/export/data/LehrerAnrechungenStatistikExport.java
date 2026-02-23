package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die nichtunterrichtlichen Tätigkeiten / Anrechungen (L65) der Lehrer.
 */
@XmlRootElement
@Schema(description = "Die nichtunterrichtlichen Tätigkeiten / Anrechungen (L65) der Lehrer")
@TranspilerDTO
public class LehrerAnrechungenStatistikExport {

	/** Die Anrechungsstunden zum Anrechnungsgrund. */
	@Schema(description = "die Anrechungsstunden zum Anrechnungsgrund", example = "20.5")
	public @NotNull double anrechungsstunden = 0.0;

	/** Satzschlüssel: Die nicht unterrichtliche Tätigkeit bzw. der Anrechnungsgrund. */
	@Schema(description = "satzschlüssel: Die nicht unterrichtliche Tätigkeit bzw. der Anrechnungsgrund", example = "510")
	public @NotNull String grund = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerAnrechungenStatistikExport() {
	}

}
