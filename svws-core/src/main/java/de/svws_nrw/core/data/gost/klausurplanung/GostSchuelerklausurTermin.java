package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Gost-Schülerklausurtermin.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem Gost-Schülerklausurtermin.")
@TranspilerDTO
public class GostSchuelerklausurTermin {

	/** Die ID des Schülerklausurtermins. */
	@Schema(description = "die ID des Schülerklausurtermins.", example = "815")
	public long id = -1;

	/** Die ID der zugehörigen Schülerklausur. */
	@Schema(description = "die ID der zugehörigen Schülerklausur.", example = "517")
	public long idSchuelerklausur = -1;

	/** Die Folgenummer der Schülerklausur, 0 falls es sich um den Haupttermin handelt, 1 der erste Nachschreibtermin ... */
	@Schema(description = "die Folgenummer der Schülerklausur, 0 falls es sich um den Haupttermin handelt, 1 der erste Nachschreibtermin ...", example = "0")
	public int folgeNr = -1;

	/** Die ID des Klausurtermins, falls schon gesetzt. */
	@Schema(description = "die ID des Klausurtermins, falls schon gesetzt.", example = "4711")
	public Long idTermin = null;

	/** Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins. */
	@Schema(description = "die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins", example = "540")
	public Integer startzeit = null;

	/** Die textuelle Bemerkung zum Schülerklausurtermin, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zum Schülerklausurtermin, sofern vorhanden", example = "Krankheit (Attest)")
	public String bemerkung = null;

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		// return another != null && another instanceof GostSchuelerklausurTermin skt && this.id == skt.id; (Syntax von Transpiler nicht unterstützt)
		return (another instanceof GostSchuelerklausurTermin) && (this.id == ((GostSchuelerklausurTermin) another).id);
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
	public GostSchuelerklausurTermin() {
		super();
	}

}
