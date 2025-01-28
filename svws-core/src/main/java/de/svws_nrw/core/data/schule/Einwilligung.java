package de.svws_nrw.core.data.schule;


import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten einer Einwilligung bei einem Schüler.
 */
@XmlRootElement
@Schema(description = "Die Daten der Einwilligung.")
@TranspilerDTO
public class Einwilligung {

	/** Die ID des zugehörigen Schülers. */
	@Schema(description = "die ID des zugehörigen Schülers.", example = "4711")
	public long idSchueler;

	/** Die ID der Einwilligungsart. */
	@Schema(description = "die ID der Einwilligungsart", example = "4713")
	public long idEinwilligungsart;

	/** Der Status der Einwilligung (erteilt/nicht erteilt). */
	@Schema(description = "der Status der Einwilligung (erteilt oder nicht erteilt)", example = "true")
	public boolean status;

	/** Der Status der Abfrage der Einwilligung (abgefragt/nicht abgefragt). */
	@Schema(description = "der Status der Abfrage der Einwilligung (abgefragt/nicht abgefragt)", example = "true")
	public boolean abgefragt;

}
