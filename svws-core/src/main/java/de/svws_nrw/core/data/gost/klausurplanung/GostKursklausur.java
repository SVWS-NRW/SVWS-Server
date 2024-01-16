package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Gost-Kursklausur.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Gost-Kursklausur")
@TranspilerDTO
public class GostKursklausur {

	/** Die ID der Kursklausur. */
	@Schema(description = "die ID der Kursklausur", example = "815")
	public long id = -1;

	/** Die ID der Klausur-Vorgabe. */
	@Schema(description = "die ID der Klausur-Vorgabe", example = "4711")
	public long idVorgabe = -1;

	/** Die ID des Klausurkurses. */
	@Schema(description = "die ID des Klausurkurses", example = "9876")
	public long idKurs = -1;

	/** Die ID des Klausurtermins, sofern schon gesetzt. */
	@Schema(description = "die ID des Klausurtermins, sofern schon gesetzt", example = "5555")
	public Long idTermin = null;

	/** Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins. */
	@Schema(description = "die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins", example = "540")
	public Integer startzeit = null;

	/** Die textuelle Bemerkung zur Kursklausur, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zur Kursklausur, sofern vorhanden", example = "Zentrale Vergleichsklausur")
	public String bemerkung = null;

}
