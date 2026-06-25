package de.svws_nrw.service.lehrer.funktion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LehrerFunktionCreateRequest {

	/** Die ID der Lehrerabschnittsdaten. */
	@Schema(description = "Die ID der Lehrerabschnittsdaten.", example = "4712")
	@NotNull
	public Long idAbschnittsdaten;

	/** Die ID in dem Katalog der schulspezifischen Lehrerfunktionen. */
	@Schema(description = "Die ID in dem Katalog der schulspezifischen Lehrerfunktionen.", example = "4713")
	@NotNull
	public Long idFunktion;

}
