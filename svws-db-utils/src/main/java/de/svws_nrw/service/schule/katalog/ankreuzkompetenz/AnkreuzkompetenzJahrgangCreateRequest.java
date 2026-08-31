package de.svws_nrw.service.schule.katalog.ankreuzkompetenz;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class AnkreuzkompetenzJahrgangCreateRequest {

	/** Die ID der Ankreuzkompetenz */
	@Schema(description = "die ID der Ankreuzkompetenz", example = "4")
	@NotNull
	public Long idAnkreuzkompetenz;

	/** Die ID des Jahrgangs */
	@Schema(description = "die ID des Jahrgangs", example = "3")
	@NotNull
	public Long idJahrgang;

}
