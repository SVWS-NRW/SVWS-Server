package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *  Die erteilten Stunden nach Bildungsbereich (nur FW) (L68).
 */
@XmlRootElement
@Schema(description = "Die erteilten Stunden nach Bildungsbereich (nur FW) (L68)")
@TranspilerDTO
public class LehrerErteilteStundenStatistikExport {

	/** Satzschlüssel: Bereich in dem die Stunden erteilt werden. */
	@Schema(description = "satzschlüssel: bereich in dem die Stunden erteilt werden", example = "SI")
	public @NotNull String bereich = "";

	/** Erteilte Stunden zum Bereich */
	@Schema(description = "erteilte Stunden zum Bereich", example = "3.0")
	public @NotNull double erteilteStunden = 0.0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerErteilteStundenStatistikExport() {
		// leer
	}

}
