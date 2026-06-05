package de.svws_nrw.service.schule.schulleitung;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SchulleitungPatchRequest {

	/** Die ID der Leitungsfunktion aus dem Katalog K_Schulfunktionen (z. B. 1 = Schulleitung, 2 = Koordination, 3 = Schulverwaltung). */
	@Schema(description = "Die ID der Leitungsfunktion aus dem Katalog der Leitungsfunktionen (K_Schulfunktionen).", example = "1")
	@NotNull
	public JsonNullable<Long> idLeitungsfunktion = JsonNullable.undefined();

	/** Der Funktionstext der Leitungsfunktion. */
	@Schema(description = "Der optionale Funktionstext der Leitungsfunktion.", example = "Schulleitung")
	@NotBlank
	@Size(max = 255)
	@NoLeadingOrTrailingWhitespaces
	public JsonNullable<String> bezeichnung = JsonNullable.undefined();

	/** Das Datum, ab dem die Leitungsfunktion gilt, im YYYY-MM-DD Format. */
	@Schema(description = "Das Datum, ab dem die Leitungsfunktion gilt (Format: YYYY-MM-DD).", example = "2023-08-01")
	public JsonNullable<@ValidDateFormat String> datumBeginnLeitungsfunktion = JsonNullable.undefined();

	/** Das Datum, bis zu dem die Leitungsfunktion gilt, im YYYY-MM-DD Format. */
	@Schema(description = "Das Datum, bis zu dem die Leitungsfunktion gilt (Format: YYYY-MM-DD).", example = "2024-07-31")
	public JsonNullable<@ValidDateFormat String> datumEndeLeitungsfunktion = JsonNullable.undefined();

}
