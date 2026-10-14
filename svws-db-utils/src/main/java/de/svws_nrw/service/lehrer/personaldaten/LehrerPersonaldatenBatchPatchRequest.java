package de.svws_nrw.service.lehrer.personaldaten;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class LehrerPersonaldatenBatchPatchRequest extends LehrerPersonaldatenPatchRequest {

	/** ID der LehrerPersonaldaten. */
	@Schema(description = "ID der LehrerPersonaldaten.", example = "126784")
	@NotNull
	public Long id;

}
