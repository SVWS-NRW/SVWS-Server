package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Lehrbefähigungen der Lehrer (L64).
 */
@XmlRootElement
@Schema(description = "die Lehrbefähigungen der Lehrer (L64)")
@TranspilerDTO
public class LehrerLehrbefaehigungenStatistikExport {

	/** Satzschlüssel: Eine Lehrbefähigung eines Lehrers. */
	@Schema(description = "satzschlüssel: Eine Lehrbefähigung eines Lehrers", example = "GE")
	public @NotNull String lehrbefaehigung = "";

	/** Die Qualifikation zu der Lehrbefähigung. */
	@Schema(description = "die Qualifikation zu der Lehrbefähigung", example = "1")
	public @NotNull String qualifikation = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerLehrbefaehigungenStatistikExport() {
		// leer
	}

}
