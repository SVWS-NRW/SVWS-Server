package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Mehrleistungen der Lehrer (L66).
 */
@XmlRootElement
@Schema(description = "Die Mehrleistungen der Lehrer (L66)")
@TranspilerDTO
public class LehrerMehrleistungenStatistikExport {

	/** Die Mehrleistungsstunden zur Mehrleistung. */
	@Schema(description = "die Mehrleistungsstunden zur Mehrleistung", example = "20.5")
	public @NotNull double mehrleistungsstunden = 0.0;

	/** Satzschlüssel: Die Mehrleistung. */
	@Schema(description = "satzschlüssel: Die Mehrleistung", example = "110")
	public @NotNull String grund = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerMehrleistungenStatistikExport() {
		// leer
	}

}
