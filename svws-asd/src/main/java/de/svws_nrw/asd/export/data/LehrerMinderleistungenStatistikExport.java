package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Minderleistungen der Lehrer (L67).
 */
@XmlRootElement
@Schema(description = "Die Minderleistungen der Lehrer (L67)")
@TranspilerDTO
public class LehrerMinderleistungenStatistikExport {

	/** Die Minderleistungsstunden zur Minderleistung. */
	@Schema(description = "die Minderleistungsstunden zur Minderleistung", example = "3.0")
	public @NotNull double minderleistungsstunden = 0.0;

	/** Satzschlüssel: Die Minderleistung. */
	@Schema(description = "satzschlüssel: Die Minderleistung", example = "200")
	public @NotNull String grund = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerMinderleistungenStatistikExport() {
	}

}
