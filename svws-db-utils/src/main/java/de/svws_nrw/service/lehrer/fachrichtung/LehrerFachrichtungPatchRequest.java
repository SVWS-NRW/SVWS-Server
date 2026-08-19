package de.svws_nrw.service.lehrer.fachrichtung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

public class LehrerFachrichtungPatchRequest {

	/** Die ID des Lehramteintrags des Lehrers. */
	@Schema(description = "Die ID des Lehramteintrags des Lehrers.", example = "4712")
	public JsonNullable<@NotNull Long> idLehramt = JsonNullable.undefined();

	/** Die Katalog-ID der Fachrichtung. */
	@Schema(description = "Die Katalog-ID der Fachrichtung.", example = "4712")
	public JsonNullable<@NotNull Long> idFachrichtung = JsonNullable.undefined();

	/** Die Katalog-ID des Anerkennungsgrund für die Fachrichtung. */
	@Schema(description = "Die Katalog-ID des Anerkennungsgrund für die Fachrichtung.", example = "4713")
	public JsonNullable<Long> idAnerkennungsgrund = JsonNullable.undefined();

}
