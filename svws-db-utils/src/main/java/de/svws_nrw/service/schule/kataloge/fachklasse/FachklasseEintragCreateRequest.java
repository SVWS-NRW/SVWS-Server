package de.svws_nrw.service.schule.kataloge.fachklasse;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FachklasseEintragCreateRequest {


	/** Die Bezeichnung */
	@Schema(description = "Die Bezeichnung", example = "Anlagenmechaniker/-in")
	@NotBlank
	@Size(max = 100)
	@NoLeadingOrTrailingWhitespaces
	public String bezeichnung;

	/** Das Kürzel */
	@Schema(description = "Das Kürzel", example = "AM")
	@NoWhitespaces
	@Size(max = 100)
	public String kuerzel;

	/** Die ID der Fachklasse (CoreType) */
	@Schema(description = "Die ID der Fachklasse (CoreType)", example = "5000")
	@NotNull
	public Long idFachklasse;

	/** Die ID der Schulgliederung */
	@Schema(description = "Die ID der Schulgliederung", example = "1001000")
	@NotNull
	public Long idSchulgliederung;

	/** Die Sichtbarkeit */
	@Schema(description = "Die Sichtbarkeit", example = "true")
	public boolean istSichtbar;

	/** Die Sortierung */
	@Schema(description = "Die Sortierung", example = "32000")
	@Max(32000)
	public Integer sortierung;

}
