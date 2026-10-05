package de.svws_nrw.service.schulwechsel.dokument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.ValidDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Request-DTO zum teilweisen Aktualisieren eines {@code SchulwechselDokument}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchulwechselDokumentPatchRequest {

	/** Der Dateiname des Dokuments. */
	@Schema(description = "Der Dateiname des Dokuments", example = "schulwechsel.xml")
	public JsonNullable<@Size(max = 100) @NotBlank String> fileName = JsonNullable.undefined();

	/** Das XML-Dokument. */
	@Schema(description = "Das Dokument selbst")
	public JsonNullable<@NotBlank String> xmlDocument = JsonNullable.undefined();

	/** Der Zeitpunkt der Erstellung. */
	@Schema(description = "Der Zeitpunkt der Erstellung", example = "2025-04-12")
	public JsonNullable<@NotBlank @ValidDateTime String> createdAt = JsonNullable.undefined();

	/** Der Zeitpunkt der letzten Änderung. */
	@Schema(description = "Der Zeitpunkt der letzten Änderung", example = "2025-04-12")
	public JsonNullable<@NotBlank @ValidDateTime String> lastModified = JsonNullable.undefined();

}
