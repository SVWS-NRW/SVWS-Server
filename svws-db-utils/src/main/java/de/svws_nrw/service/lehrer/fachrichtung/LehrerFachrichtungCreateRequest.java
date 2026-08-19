package de.svws_nrw.service.lehrer.fachrichtung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class LehrerFachrichtungCreateRequest {

	/** Die ID des Lehramteintrags des Lehrers. */
	@Schema(description = "Die ID des Lehramteintrags des Lehrers.", example = "4712")
	@NotNull
	public Long idLehramt;

	/** Die Katalog-ID der Fachrichtung. */
	@Schema(description = "Die Katalog-ID der Fachrichtung.", example = "4712")
	@NotNull
	public Long idFachrichtung;

	/** Die Katalog-ID des Anerkennungsgrund für die Fachrichtung. */
	@Schema(description = "Die Katalog-ID des Anerkennungsgrund für die Fachrichtung.", example = "4713")
	public Long idAnerkennungsgrund;

}
