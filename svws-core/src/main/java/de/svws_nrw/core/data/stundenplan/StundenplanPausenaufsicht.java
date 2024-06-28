package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den Pausenaufsichten im Stundenplan.
 */
@XmlRootElement
@Schema(description = "die Pausenaufsicht im Stundenplan.")
@TranspilerDTO
public class StundenplanPausenaufsicht {

	/** Die ID der Pausenaufsicht */
	@Schema(description = "die ID der Pausenaufsicht", example = "42")
	public long id = -1;

	/** Die ID der {@link StundenplanPausenzeit}. */
	@Schema(description = "die ID der Pausenzeit", example = "815")
	public long idPausenzeit = -1;

	/** Die ID des {@link StundenplanLehrer} der Aufsicht führt. */
	@Schema(description = "die ID des Lehrers, der Aufsicht führt", example = "89")
	public long idLehrer = -1;

	/** Die Zuordnung der Aufsichtsbereiche ({@link StundenplanAufsichtsbereich}) zu den Pausenaufsichten ({@link StundenplanPausenaufsicht}) und dem Wochentyp. */
	@ArraySchema(schema = @Schema(implementation = StundenplanPausenaufsichtBereich.class,
			description = "Ein Array mit den Zuordnungen der Aufsichtsbeiche zu den Pausenaufsichten und dem Wochentyp."))
	public @NotNull List<@NotNull StundenplanPausenaufsichtBereich> bereiche = new ArrayList<>();

}
