package de.svws_nrw.service.schule.katalog.merkmal;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class MerkmalPatchRequest {

	/** Das Kuerzel des Merkmals */
	@Schema(description = "Das Kuerzel des Merkmals", example = "GANZTAG")
	@NotBlank
	@Size(max = 10)
	@NoWhitespaces
	public JsonNullable<String> kuerzel = JsonNullable.undefined();

	/** Die Bezeichnung des Merkmals */
	@Schema(description = "Die Bezeichnung des Merkmals", example = "Ganztagsschule")
	@NotBlank
	@Size(max = 100)
	@NoLeadingOrTrailingWhitespaces
	public JsonNullable<String> bezeichnung = JsonNullable.undefined();

	/** Gibt an, ob das Merkmal einer Schule zugewiesen werden kann */
	@Schema(description = "Gibt an, ob das Merkmal einer Schule zugewiesen werden kann", example = "true")
	public JsonNullable<Boolean> istSchulmerkmal = JsonNullable.undefined();

	/** Gibt an, ob das Merkmal einem Schueler zugewiesen werden kann */
	@Schema(description = "Gibt an, ob das Merkmal einem Schueler zugewiesen werden kann", example = "true")
	public JsonNullable<Boolean> istSchuelermerkmal = JsonNullable.undefined();

}
