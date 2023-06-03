package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Jahrgang in einem Stundenplan.
 */
@XmlRootElement
@Schema(description = "ein Jahrgang in einem Stundenplan")
@TranspilerDTO
public class StundenplanJahrgang {

	/** Die ID des Jahrgangs. */
	@Schema(description = "die ID der Jahrgangs", example = "4711")
	public long id = -1;

	/** Das Kürzel des Jahrgangs. */
	@Schema(description = "das Kürzel des Jahrgangs", example = "06")
	public @NotNull String kuerzel = "";

	/** Die Bezeichnung des Jahrgangs. */
	@Schema(description = "Die Bezeichnung des Jahrgangs", example = "6. Jahrgang")
	public @NotNull String bezeichnung = "";

}
