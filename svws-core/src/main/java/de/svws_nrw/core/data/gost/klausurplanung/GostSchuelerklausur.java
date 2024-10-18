package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Gost-Schülerklausur.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Gost-Schülerklausur.")
@TranspilerDTO
public class GostSchuelerklausur {

	/** Die ID der Schülerklausur. */
	@Schema(description = "die ID der Schülerklausur.", example = "815")
	public long id = -1;

	/** Die ID der zugehörigen Kursklausur. */
	@Schema(description = "die ID der zugehörigen Kursklausur.", example = "4711")
	public long idKursklausur = -1;

	/** Die ID des zugehörigen Schülers. */
	@Schema(description = "die ID des zugehörigen Schülers.", example = "2242")
	public long idSchueler = -1;

	/** Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zur Schülerklausur, sofern vorhanden", example = "Nachteilsausgleich!")
	public String bemerkung = null;

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another instanceof GostSchuelerklausur) && (this.id == ((GostSchuelerklausur) another).id);
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

	/**
	 * Default-Konstruktor
	 */
	public GostSchuelerklausur() {
		super();
	}

}
