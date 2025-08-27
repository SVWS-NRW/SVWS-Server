package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt einen allgemeinen Termin.
 */
@XmlRootElement
@Schema(description = "Ein allgemeiner Termin.")
@TranspilerDTO
public class TerminKatalogEintrag extends CoreTypeDataNurSchulformen {

	/** Der Anfang der Zeitspanne (Datum und ggf. Uhrzeit), für welche der Termin gilt. Die Uhrzeit ist optional bei ganztägigen Terminen. */
	@Schema(description = "der Anfang der Zeitspanne (Datum), für welche der Termin gilt.", example = "2025-08-27 09:30")
	public @NotNull String von = "";

	/** Das Ende der Zeitspanne (Datum und ggf. Uhrzeit), für welche der Termin gilt. Die Uhrzeit ist optional bei ganztägigen Terminen. */
	@Schema(description = "das Ende der Zeitspanne (Datum), für welche der Termin gilt", example = "2025-08-27 10:30")
	public @NotNull String bis = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public TerminKatalogEintrag() {
		// leer
	}

}
