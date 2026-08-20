package de.svws_nrw.service.lehrer.lehrbefaehigung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class LehrerLehrbefaehigungCreateRequest {

	/** Die ID des Lehramteintrags des Lehrers. */
	@Schema(description = "Die ID des Lehramteintrags des Lehrers.", example = "4712")
	@NotNull
	public Long idLehramt;

	/** Die Katalog-ID der Lehrbefähigung. */
	@Schema(description = "Die Katalog-ID der Lehrbefähigung.", example = "4712")
	@NotNull
	public Long idLehrbefaehigung;

	/** Die Katalog-ID des Anerkennungsgrund für die Lehrbefähigung. */
	@Schema(description = "Die Katalog-ID des Anerkennungsgrund für die Lehrbefähigung.", example = "4713")
	public Long idAnerkennungsgrund;

}
