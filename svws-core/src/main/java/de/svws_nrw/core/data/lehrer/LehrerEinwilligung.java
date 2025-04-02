package de.svws_nrw.core.data.lehrer;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten einer Einwilligung bei einem Lehrer.
 */
@XmlRootElement
@Schema(description = "Die Daten der Einwilligung.")
@TranspilerDTO
public class LehrerEinwilligung {

	/** Die ID des zugehörigen Lehrers. */
	@Schema(description = "Die ID des zugehörigen Lehrers", example = "12", accessMode = Schema.AccessMode.READ_ONLY)
	public long idLehrer;

	/** Die ID der Einwilligungsart. */
	@Schema(description = "die ID der Einwilligungsart", example = "12", accessMode = Schema.AccessMode.READ_ONLY)
	public long idEinwilligungsart;

	/** Der Status der Einwilligung (erteilt/nicht erteilt). */
	@Schema(description = "der Status der Einwilligung (zugestimmt/nicht zugestimmt)", example = "true")
	public boolean istZugestimmt;

	/** Der Status der Abfrage der Einwilligung (abgefragt/nicht abgefragt). */
	@Schema(description = "der Status der Abfrage der Einwilligung (abgefragt/nicht abgefragt)", example = "true")
	public boolean istAbgefragt;
}
