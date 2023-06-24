package de.svws_nrw.core.data.gost.klausuren;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Gost-Klausurraumstunde.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Gost-Klausurraumstunde.")
@TranspilerDTO
public class GostKlausurraumstunde {

	/** Die ID der Klausurraumstunde. */
	@Schema(description = "die ID der Klausurraumstunde", example = "815")
	public long id = -1;

	/** Die ID des Klausurraums. */
	@Schema(description = "die ID des Klausurraums", example = "2242")
	public long idRaum = -1;

	/** Die ID des Zeitrasters. */
	@Schema(description = "die ID des Zeitrasters", example = "221")
	public long idZeitraster = -1;

}
