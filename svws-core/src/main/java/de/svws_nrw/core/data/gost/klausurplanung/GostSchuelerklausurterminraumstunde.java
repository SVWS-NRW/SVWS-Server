package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Gost-Schuelerklausurraumstunde.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Gost-Schuelerklausurraumstunde.")
@TranspilerDTO
public class GostSchuelerklausurterminraumstunde {

	/** Die ID des zugehörigen {@link GostSchuelerklausurTermin}s. */
	@Schema(description = "die ID des zugehörigen Schülerklausurtermins", example = "815")
	public long idSchuelerklausurtermin = -1;

	/** Die ID der zugehörigen {@link GostKlausurraumstunde}. */
	@Schema(description = "die ID der zugehörigen Klausurraumstunde", example = "2242")
	public long idRaumstunde = -1;

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another != null) && (another instanceof GostSchuelerklausurterminraumstunde)
				&& (this.idSchuelerklausurtermin == ((GostSchuelerklausurterminraumstunde) another).idSchuelerklausurtermin)
				&& (this.idRaumstunde == ((GostSchuelerklausurterminraumstunde) another).idRaumstunde);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(idSchuelerklausurtermin);
	}

	/**
	 * Default-Konstruktor
	 */
	public GostSchuelerklausurterminraumstunde() {
		super();
	}

}
