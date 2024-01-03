package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu dem Stundenplan eines Schülers.
 */
@XmlRootElement
@Schema(description = "der Stundenplan eines Schülers.")
@TranspilerDTO
public class GostSchuelerklausur {

	/** Die ID des Stundenplans. */
	@Schema(description = "die ID des Stundenplans", example = "815")
	public long id = -1;

	/** Die textuelle Beschreibung des Stundenplans. */
	@Schema(description = "die textuelle Beschreibung des Stundenplans", example = "Stundenplan zum Schuljahresanfang")
	public long idKursklausur = -1;

	/** Das Zeitraster des Stundenplans. */
	@Schema(description = "das Zeitraster des Stundenplans")
	public long idSchueler = -1;

	/** Die Liste der IDs der zugehörigen Termine. */
	@Schema(description = "die Liste der IDs der zugehörigen Termine", example = "[ 5590, 5591, 5592, ... ]")
	public @NotNull List<@NotNull GostSchuelerklausurTermin> schuelerklausurTermine = new ArrayList<>();

	/** Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zur Schülerklausur, sofern vorhanden", example = "Zentrale Vergleichsklausur")
	public String bemerkung = null;

}
