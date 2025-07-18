package de.svws_nrw.asd.data.lehrer;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt eine Lehrbefähigung in Bezug auf das Lehramt eines Lehrers.
 */
@XmlRootElement
@Schema(description = "Eine Lehrbefähigung zu einem Lehramt eines Lehrers.")
@TranspilerDTO
public class LehrerLehrbefaehigungEintrag {

	/** Die ID des Eintrages. */
	@Schema(description = "Die ID des Eintrages.", example = "4711")
	public long id;

	/** Die ID des Lehramteintrags des Lehrers. */
	@Schema(description = "Die ID des Lehramteintrags des Lehrers.", example = "4712")
	public long idLehramt;

	/** Die Katalog-ID der Lehrbefähigung. */
	@Schema(description = "Die Katalog-ID der Lehrbefähigung.", example = "4712")
	public long idLehrbefaehigung;

	/** Die Katalog-ID des Anerkennungsgrund für die Lehrbefähigung. */
	@Schema(description = "Die Katalog-ID des Anerkennungsgrund für die Lehrbefähigung.", example = "4713")
	public Long idAnerkennungsgrund;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerLehrbefaehigungEintrag() {
		// leer
	}

}
