package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den Zuordnungen von Kalenderwochen zu den Wochentypen,
 * falls der Stundenplan mehrere Wochentypen unterstützt.
 */
@XmlRootElement
@Schema(description = "der Wochentyp der entsprechenden Kalenderwoche.")
@TranspilerDTO
public class StundenplanKalenderwochenzuordnung {

	/** Die ID der Kalenderwochenzuordnung. */
	@Schema(description = "die ID der Kalenderwochenzuordnung", example = "4711")
	public long id = -1;

	/** Das Kalenderjahr der Zuordnung */
	@Schema(description = "das Kalenderjahr der Zuordnung ", example = "2051")
	public int jahr = -1;

	/** Die Kalenderwoche in dem Jahr. */
	@Schema(description = "die Kalenderwoche in dem Jahr", example = "48")
	public int kw = -1;

	/** Der Wochentyp, der der Kalenderwoche zugeordnet ist (z.B. eine A- bzw. B-Wochen, d.h. 1 bzw. 2). Muss größer als 0 sein.*/
	@Schema(description = "der Wochentyp, der der Kalenderwoche zugeordnet ist (z.B. eine A- bzw. B-Wochen, d.h. 1 bzw. 2). Muss größer als 0 sein.", example = "2")
	public int wochentyp = 0;

}
