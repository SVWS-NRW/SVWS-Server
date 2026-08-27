package de.svws_nrw.service.schueler.stammdaten;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class SchuelerStammdatenBatchPatchRequest extends SchuelerStammdatenPatchRequest {

	/** ID der SchülerStammdaten. */
	@Schema(description = "ID der SchülerStammdaten.", example = "126784")
	@NotNull
	public Long id;

}
