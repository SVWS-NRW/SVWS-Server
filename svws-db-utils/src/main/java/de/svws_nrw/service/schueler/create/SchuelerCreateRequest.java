package de.svws_nrw.service.schueler.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enthält die Attribute, die beim Neuanlegen eines Schülers über den {@link SchuelerCreateService} angegeben werden können.
 * Zusätzlich zu den Stammdaten des {@link SchuelerStammdatenCreateRequest} werden Angaben zum Lernabschnitt
 * und zum Schulbesuch entgegengenommen. Die Feldnamen stimmen mit dem Core-DTO {@code SchuelerNeu} überein.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchuelerCreateRequest extends SchuelerStammdatenCreateRequest {

	// --- Lernabschnittsdaten ---

	/** Die ID des Jahrgangs des Schülers. */
	@Schema(description = "Die ID des Jahrgangs des Schülers.", example = "4")
	public Long idJahrgang;

	/** Die ID der Klasse des Schülers. */
	@Schema(description = "Die ID der Klasse des Schülers.", example = "10")
	public Long idKlasse;

	// --- Schulbesuchsdaten ---

	/** Die ID der Einschulungsart (nur bei Grundschule). */
	@Schema(description = "Die ID der Einschulungsart (nur bei Grundschule).", example = "51")
	public Long idGrundschuleEinschulungsart;

}
