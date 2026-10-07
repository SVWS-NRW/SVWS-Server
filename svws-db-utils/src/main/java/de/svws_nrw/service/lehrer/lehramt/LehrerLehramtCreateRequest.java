package de.svws_nrw.service.lehrer.lehramt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class LehrerLehramtCreateRequest {

	/** Die ID des Lehrers. */
	@Schema(description = "Die ID des Lehrers.", example = "4711")
	@NotNull
	public Long idLehrer;

	/** Die ID des Lehramtes. */
	@Schema(description = "Die ID des Lehramtes.", example = "82")
	@NotNull
	public Long idKatalogLehramt;

	/** Die ID des Anerkennungsgrund für das Lehramt. */
	@Schema(description = "Die Katalog-ID des Anerkennungsgrund für das Lehramt.", example = "1")
	public Long idAnerkennungsgrund;

}
