package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.Wochentag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den beaufsichtigten Pausenzeiten eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "eine Pausenzeit eines Stundenplans.")
@TranspilerDTO
public class StundenplanPausenzeit {

	/** Die ID der Pausenzeit. */
	@Schema(description = "die ID der Pausenzeit", example = "4711")
	public long id = -1;

	/** Der {@link Wochentag} für die Pausenzeit (1=Montag, 2=Dienstag, ..., 7=Sonntag) */
	@Schema(description = "der Wochentag für die Pausenzeit (1=Montag, 2=Dienstag, ..., 7=Sonntag)", example = "1")
	public int wochentag = -1;

	/** Die Uhrzeit in Minuten seit 0 Uhr, wann die Pause beginnt. NULL bedeutet "noch nicht definiert". */
	@Schema(description = "die Uhrzeit in Minuten seit 0 Uhr, wann die Pause beginnt", example = "610")
	public Integer beginn = null;

	/** Die Uhrzeit in Minuten seit 0 Uhr, wann die Pause endet. NULL bedeutet "noch nicht definiert".  */
	@Schema(description = "die Uhrzeit in Minuten seit 0 Uhr, wann die Pause endet", example = "630")
	public Integer ende = null;

}
