package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen für die Zuordnung einer Pausenaufsicht im Stundenplan
 * zu einem Aufsichtsbereich.
 */
@XmlRootElement
@Schema(description = "die Zuordnung einer Pausenaufsicht im Stundenplan zu einem Aufsichtsbereich.")
@TranspilerDTO
public class StundenplanPausenaufsichtBereich {

	/** Die ID der Zuordnung */
	@Schema(description = "die ID der Zuordnung", example = "42")
	public long id = -1;

	/** Die ID der {@link StundenplanPausenaufsicht}. */
	@Schema(description = "die ID der Pausenaufsicht", example = "815")
	public long idPausenaufsicht = -1;

	/** Die ID des {@link StundenplanAufsichtsbereich}. */
	@Schema(description = "die ID des Aufsichtsbereichs", example = "89")
	public long idAufsichtsbereich = -1;

	/** Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 */
	@Schema(description = "der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 ", example = "0")
	public int wochentyp = -1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public StundenplanPausenaufsichtBereich() {
		// leer
	}

}
