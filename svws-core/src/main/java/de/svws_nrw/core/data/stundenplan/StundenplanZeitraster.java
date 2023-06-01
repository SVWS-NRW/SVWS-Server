package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.Wochentag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu dem Zeitraster eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "das Zeitraster eines Stundenplans.")
@TranspilerDTO
public class StundenplanZeitraster {

	/** Die ID des Zeitraster-Eintrages. */
	@Schema(description = "die ID des Zeitraster-Eintrages", example = "4711")
	public long id = -1;

	/** Der {@link Wochentag} an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag) */
	@Schema(description = "der Wochentag an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)", example = "1")
	public int wochentag = 1;

	/** Die Nummer der Unterrichtsstunde an dem Wochentag */
	@Schema(description = "die Nummer der Unterrichtsstunde an dem Wochentag", example = "1")
	public int unterrichtstunde = 1;

	/** Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde beginnt. NULL bedeutet "noch nicht definiert". */
	@Schema(description = "die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde beginnt", example = "430")
	public Integer stundenbeginn = null;

	/** Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde endet. NULL bedeutet "noch nicht definiert". */
	@Schema(description = "die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde endet", example = "475")
	public Integer stundenende = null;

}
