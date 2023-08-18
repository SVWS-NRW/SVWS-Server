package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Gost-Kalenderinformation
 */
@XmlRootElement
@Schema(description = "eine Kalenderinformation.")
@TranspilerDTO
public class GostKlausurenKalenderinformation {

	/** Die ID der Kalenderinformation. */
	@Schema(description = "die ID der Kalenderinformation", example = "4711")
	public long id = -1;

	/** Die Bezeichnung der Kalenderinformation. */
	@Schema(description = "die Bezeichnung der Kalenderinformation", example = "Elternsprechtag")
	public @NotNull String bezeichnung = "";

	/** Das Startdatum der Kalenderinformation. */
	@Schema(description = "das Startdatum der Kalenderinformation", example = "29.02.2025")
	public String startdatum = null;

	/** Die Startzeit der Kalenderinformation in Minuten seit 0 Uhr. */
	@Schema(description = "die Startzeit der Kalenderinformation in Minuten seit 0 Uhr", example = "540")
	public Integer startzeit = null;

	/** Das Enddatum der Kalenderinformation. */
	@Schema(description = "das Enddatum der Kalenderinformation", example = "01.03.2025")
	public String enddatum = null;

	/** Die Endzeit der Kalenderinformation in Minuten seit 0 Uhr. */
	@Schema(description = "die Endzeit der Kalenderinformation in Minuten seit 0 Uhr", example = "660")
	public Integer endzeit = null;

	/** Die textuelle Bemerkung zur Kalenderinformation, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zur Kalenderinformation, sofern vorhanden", example = "Im Anschluss kein regulärer Unterricht.")
	public String bemerkung = null;

	/** Die Information, ob es sich um einen Sperrtermin handelt. */
	@Schema(description = "die Information, ob es sich um einen Sperrtermin handelt", example = "true")
	public boolean istSperrtermin = false;

}
