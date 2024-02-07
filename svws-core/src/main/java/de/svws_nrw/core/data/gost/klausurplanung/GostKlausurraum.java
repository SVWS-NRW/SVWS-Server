package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Gost-Klausurraum.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem Gost-Klausurraum.")
@TranspilerDTO
public class GostKlausurraum {

	/** Die ID des Klausurraums. */
	@Schema(description = "die ID des Klausurraums", example = "815")
	public long id = -1;

	/** Die ID des Klausurtermins. */
	@Schema(description = "die ID des Klausurtermins", example = "2242")
	public long idTermin = -1;

	/** Die ID des Stundenplan_Raumes. */
	@Schema(description = "die ID des Stundenplan_Raumes", example = "221")
	public Long idStundenplanRaum = null;

	/** Die textuelle Bemerkung zum Klausurraum, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zum Klausurraum, sofern vorhanden", example = "Bemerkung")
	public String bemerkung = null;

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return another != null && another instanceof GostKlausurraum && this.id == ((GostKlausurraum) another).id;
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
