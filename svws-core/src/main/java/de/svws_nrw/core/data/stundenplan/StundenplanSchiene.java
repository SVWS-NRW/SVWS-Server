package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den Schienen eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "eine Schiene eines Stundenplans.")
@TranspilerDTO
public class StundenplanSchiene {

	/** Die ID der Schiene. */
	@Schema(description = "die ID der Schiene", example = "4711")
	public long id = -1;

	/** Die ID des Jahrgangs, dem die Schiene zugeordnet ist. */
	@Schema(description = "die ID des Jahrgangs, dem die Schiene zugeordnet ist", example = "42")
	public long idJahrgang = -1;

	/** Die Nummer der Schiene. */
	@Schema(description = "die Nummer der Schiene", example = "3")
	public int nummer = -1;

	/** Die Bezeichnung der Schiene. */
	@Schema(description = "die Bezeichnung der Schiene", example = "Schiene 3")
	public @NotNull String bezeichnung = "";

}
