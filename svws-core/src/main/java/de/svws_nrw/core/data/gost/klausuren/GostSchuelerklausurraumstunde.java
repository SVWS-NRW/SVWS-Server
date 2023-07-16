package de.svws_nrw.core.data.gost.klausuren;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Gost-Schuelerklausurraumstunde.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Gost-Schuelerklausurraumstunde.")
@TranspilerDTO
public class GostSchuelerklausurraumstunde {

	/** Die ID der Schülerklausur. */
	@Schema(description = "die ID der Schülerklausur", example = "815")
	public long idSchuelerklausur = -1;

	/** Die ID der Klausurraumstunde. */
	@Schema(description = "die ID der Klausurraumstunde", example = "2242")
	public long idRaumstunde = -1;

}
