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
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurraumstunde.
	 *
	 * @param another     das Datenbankschema
	 * @return die HTTP-Antwort
	 */
	@Override
	public boolean equals(final Object another) {
		return another != null && another instanceof GostSchuelerklausurTermin && this.id == ((GostSchuelerklausurTermin) another).id;
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurraumstunde.
	 *
	 * @return die HTTP-Antwort
	 */
	@Override
	public int hashCode() {
		return (int) id;
	}

}
