package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den Pausenaufsichten im Stundenplan.
 */
@XmlRootElement
@Schema(description = "der Unterricht im Stundenplan.")
@TranspilerDTO
public class StundenplanPausenaufsicht {

	/** Die ID der Pausenaufsicht */
	@Schema(description = "die ID der Pausenaufsicht", example = "42")
	public long id = -1;

	/** Die ID der Pausenzeit */
	@Schema(description = "die ID der Pausenzeit", example = "815")
	public long idPausenzeit = -1;

	/** Die ID des Lehrers, der Aufsicht führt */
	@Schema(description = "die ID des Lehrers, der Aufsicht führt", example = "89")
	public long idLehrer = -1;

	/** Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 */
	@Schema(description = "der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 ", example = "0")
	public int wochentyp = -1;

	/** Die IDs der Aufsichtsbereiche, in denen in dieser Pausenzeit von dem Lehrer Aufsicht geführt wird. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Aufsichtsbeiche, in denen in dieser Pausenzeit von dem Lehrer Aufsicht geführt wird."))
	public @NotNull List<@NotNull Long> bereiche = new ArrayList<>();

}
