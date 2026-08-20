package de.svws_nrw.service.lehrer.lehrbefaehigung;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

public class LehrerLehrbefaehigungPatchRequest {

	/** Die ID des Lehramteintrags des Lehrers. */
	@Schema(description = "Die ID des Lehramteintrags des Lehrers.", example = "4712")
	public JsonNullable<@NotNull Long> idLehramt = JsonNullable.undefined();

	/** Die Katalog-ID der Lehrbefähigung. */
	@Schema(description = "Die Katalog-ID der Lehrbefähigung.", example = "4712")
	@NotNull
	public JsonNullable<@NotNull Long> idLehrbefaehigung = JsonNullable.undefined();

	/** Die Katalog-ID des Anerkennungsgrund für die Lehrbefähigung. */
	@Schema(description = "Die Katalog-ID des Anerkennungsgrund für die Lehrbefähigung.", example = "4713")
	public JsonNullable<@NotNull Long> idAnerkennungsgrund = JsonNullable.undefined();

}
