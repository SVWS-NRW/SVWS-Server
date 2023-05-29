package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.Wochentag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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

	/** Die Uhrzeit, wann die Pause beginnt. */
	@Schema(description = "die Zeit, wann die Pause beginnt", example = "10:10:00")
	public @NotNull String beginn = "";

	/** Die Uhrzeit, wann die Pause endet. */
	@Schema(description = "die Zeit, wann die Pause endet", example = "10:30:00")
	public @NotNull String ende = "";

}
