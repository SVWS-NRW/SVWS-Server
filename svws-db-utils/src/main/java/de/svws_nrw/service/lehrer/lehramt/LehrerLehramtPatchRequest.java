package de.svws_nrw.service.lehrer.lehramt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

public class LehrerLehramtPatchRequest {

	/** Die ID des Lehrers. */
	@Schema(description = "Die ID des Lehrers.", example = "4711")
	@NotNull
	public JsonNullable<@NotNull Long> idLehrer = JsonNullable.undefined();

	/** Die ID des Lehramtes. */
	@Schema(description = "Die ID des Lehramtes.", example = "82")
	@NotNull
	public JsonNullable<@NotNull Long> idKatalogLehramt = JsonNullable.undefined();

	/** Die ID des Anerkennungsgrund für das Lehramt. */
	@Schema(description = "Die Katalog-ID des Anerkennungsgrund für das Lehramt.", example = "1")
	public JsonNullable<Long> idAnerkennungsgrund = JsonNullable.undefined();

}
