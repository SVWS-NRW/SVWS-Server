package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu dem Stundenplan eines Schülers.
 */
@XmlRootElement
@Schema(description = "der Stundenplan eines Schülers.")
@TranspilerDTO
public class GostSchuelerklausurTermin {

	/** Die ID des Stundenplans. */
	@Schema(description = "die ID des Stundenplans", example = "815")
	public long id = -1;

	/** Die textuelle Beschreibung des Stundenplans. */
	@Schema(description = "die textuelle Beschreibung des Stundenplans", example = "Stundenplan zum Schuljahresanfang")
	public long idSchuelerklausur = -1;

	/** Die textuelle Beschreibung des Stundenplans. */
	@Schema(description = "die textuelle Beschreibung des Stundenplans", example = "Stundenplan zum Schuljahresanfang")
	public int folgeNr = -1;

	/** Das Zeitraster des Stundenplans. */
	@Schema(description = "das Zeitraster des Stundenplans")
	public Long idTermin = null;

	/** Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins. */
	@Schema(description = "die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins", example = "540")
	public Integer startzeit = null;

	/** Die textuelle Bemerkung zum Schülerklausurtermin, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zum Schülerklausurtermin, sofern vorhanden", example = "Krankheit (Attest)")
	public String bemerkung = null;

	/**
	 * Vergleicht, ob ein GostSchuelerklausurTermin-Objekt dasselbe Objekt, wie ein anderes GostSchuelerklausurTermin-Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		// return another != null && another instanceof GostSchuelerklausurTermin skt && this.id == skt.id; (Syntax von Transpiler nicht unterstützt)
		return another != null && another instanceof GostSchuelerklausurTermin && this.id == ((GostSchuelerklausurTermin) another).id;
	}

	/**
	 * Erzeugt den Hashcode zum GostSchuelerklausurTermin-Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		// return Long.hashCode(id); (vom Transpiler nicht unterstützt)
		return (int) id;
	}

}
