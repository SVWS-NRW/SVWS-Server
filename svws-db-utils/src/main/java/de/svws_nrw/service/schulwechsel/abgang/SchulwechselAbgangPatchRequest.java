package de.svws_nrw.service.schulwechsel.abgang;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.ValidDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Request-DTO zum teilweisen Aktualisieren eines {@code SchulwechselDokument}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchulwechselAbgangPatchRequest {

	/** Die Id des aktuellen Status. */
	@Schema(description = "Die Id des aktuellen Status", example = "1")
	public JsonNullable<@NotNull Integer> idStatus = JsonNullable.undefined();

	/** Der Zeitpunkt der letzten Änderung. */
	@Schema(description = "Der Zeitpunkt der letzten Änderung", example = "2025-04-12 12:34:25")
	public JsonNullable<@NotBlank @ValidDateTime String> lastModified = JsonNullable.undefined();

	/** Die Id des zugehörigen Wechseldokuments. */
	@Schema(description = "Die Id des zugehörigen Wechseldokuments", example = "12345")
	public JsonNullable<Long> idDocument = JsonNullable.undefined();

	/** Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang. */
	@Schema(description = "Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang", example = "123e4567-e89b-12d3-a456-426614174000")
	public JsonNullable<String> idSchulkindSchulbewerbung = JsonNullable.undefined();
}
