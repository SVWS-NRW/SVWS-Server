package de.svws_nrw.service.lehrer.funktion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LehrerFunktionPatchRequest {

	/** Die ID in dem Katalog der schulspezifischen Lehrerfunktionen. */
	@Schema(description = "Die ID in dem Katalog der schulspezifischen Lehrerfunktionen.", example = "4713")
	public JsonNullable<@NotNull Long> idFunktion = JsonNullable.undefined();

}
