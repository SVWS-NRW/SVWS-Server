package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den beaufsichtigten Aufsichtsbereichen eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "ein Aufsichtsbereich eines Stundenplans.")
@TranspilerDTO
public class StundenplanAufsichtsbereich {

	/** Die ID des Aufsichtsbereichs. */
	@Schema(description = "die ID des Aufsichtsbereichs", example = "4711")
	public long id = -1;

	/** Das Kürzel des Aufsichtsbereichs. */
	@Schema(description = "das Kürzel des Aufsichtsbereichs", example = "SP")
	public @NotNull String kuerzel = "";

	/** Die Beschreibung des Aufsichtsbereichs. */
	@Schema(description = "die Beschreibung des Aufsichtsbereichs", example = "Der Sportplatz für Pausensport")
	public @NotNull String beschreibung = "";

}
