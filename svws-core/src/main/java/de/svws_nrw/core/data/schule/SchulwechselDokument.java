package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Wechselvorgangsdaten eines Schüler-Abgangs.
 */
@XmlRootElement
@Schema(description = "Ein Dokument mit Bezug auf den Schulwechsel eines Schülers")
@TranspilerDTO
public class SchulwechselDokument {

	/** Die ID des Dokuments. */
	@Schema(description = "Die Id des Dokuments", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Der Dateiname des Dokuments */
	@Schema(description = "Der Dateiname des Dokuments", example = "schulwechsel.xml")
	public String fileName;

	/** Das XML-Dokument */
	@Schema(description = "Das Dokument selbst")
	public String xmlDocument;

	/** Der Zeitpunkt der Erstellung des Dokuments. */
	@Schema(description = "Der Zeitpunkt der Erstellung des Dokuments", example = "2025-04-12")
	public @NotNull String createdAt = "";

	/** Der Zeitpunkt der letzten Änderung des Dokuments. */
	@Schema(description = "Der Zeitpunkt der letzten Änderung des Dokuments", example = "2025-04-12")
	public @NotNull String lastModified = "";

}
