package de.svws_nrw.service.schulwechsel.dokument;

import de.svws_nrw.validation.constraints.ValidDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SchulwechselDokumentCreateRequest {

	/** Der Dateiname des Dokuments */
	@Schema(description = "Der Dateiname des Dokuments", example = "schulwechsel.xml")
	@NotBlank
	@Size(max = 100)
	public String fileName;

	/** Das XML-Dokument */
	@Schema(description = "Das Dokument selbst")
	@NotBlank
	public String xmlDocument;

	/** Der Zeitpunkt der letzten Statusänderung des Wechselvorgangs. */
	@Schema(description = "Der Zeitpunkt der Erstellung des Dokuments", example = "2025-04-12")
	@NotBlank
	@ValidDateTime
	public String createdAt;

	/** Der Zeitpunkt der letzten Statusänderung des Wechselvorgangs. */
	@Schema(description = "Der Zeitpunkt der letzten Änderung des Dokuments", example = "2025-04-12")
	@NotBlank
	@ValidDateTime
	public String lastModified;

}
