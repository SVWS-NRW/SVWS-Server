package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class LehrerPersonalabschnittsdatenBatchPatchRequest extends LehrerPersonalabschnittsdatenPatchRequest {

	/** ID der LehrerPersonalabschnittsdaten. */
	@Schema(description = "ID der LehrerPersonalabschnittsdaten.", example = "126784")
	@NotNull
	public Long id;

}
