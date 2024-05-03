package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
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
public class GostKlausurraumRich {

	/** Die ID des Klausurraums. */
	@Schema(description = "die ID des Klausurraums", example = "815")
	public long id = -1;

	/** Die Grösse des Raumes an Arbeitsplätzen für Schüler. */
	@Schema(description = "die Grösse des Raumes an Arbeitsplätzen für Schüler", example = "30")
	public int groesse = -1;

	/**
	 * Konstruktor zur Erstellung des Rich-Objekts.
	 *
	 * @param klausurraum     das zu vergleichende Objekt
	 * @param stundenplanraum
	 *
	 */
	public GostKlausurraumRich(final GostKlausurraum klausurraum, final StundenplanRaum stundenplanraum) {
		id = klausurraum.id;
		groesse = stundenplanraum.groesse;
	}

	/**
	 * Konstruktor für Transpiler.
	 */
	public GostKlausurraumRich() {
	}

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return another != null && another instanceof GostKlausurraumRich && this.id == ((GostKlausurraumRich) another).id;
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
