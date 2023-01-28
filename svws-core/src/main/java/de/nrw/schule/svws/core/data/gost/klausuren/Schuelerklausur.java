package de.nrw.schule.svws.core.data.gost.klausuren;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu dem Stundenplan eines Schülers.  
 */
@XmlRootElement
@Schema(description="der Stundenplan eines Schülers.")
@TranspilerDTO
public class Schuelerklausur {

	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public long idSchuelerklausur = -1;

	/** Die textuelle Beschreibung des Stundenplans. */
	@Schema(required = false, description = "die textuelle Beschreibung des Stundenplans", example="Stundenplan zum Schuljahresanfang")
	public long idKursklausur = -1;
	
	/** Das Zeitraster des Stundenplans. */
	@Schema(required = true, description = "das Zeitraster des Stundenplans")
	public long idTermin = -1;

	/** Das Zeitraster des Stundenplans. */
	@Schema(required = true, description = "das Zeitraster des Stundenplans")
	public long idSchueler = -1;
	
	/** Das Datum, ab dem der Stundenpland gültig ist. */
	@Schema(required = false, description = "das Datum, ab dem der Stundenpland gültig ist", example="1.1.1899")
	public String startzeit = null;

}
