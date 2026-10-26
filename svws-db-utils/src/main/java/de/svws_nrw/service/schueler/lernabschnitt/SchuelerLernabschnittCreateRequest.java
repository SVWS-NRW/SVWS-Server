package de.svws_nrw.service.schueler.lernabschnitt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Enthält die Attribute, die beim Anlegen eines Schülerlernabschnitts über den {@link SchuelerLernabschnittService}
 * angegeben werden können. Neben der ID des Schülers und der ID des Schuljahresabschnitts können alle Attribute
 * des {@link SchuelerLernabschnittsdatenPatchRequest} gesetzt werden. Nicht angegebene Attribute werden
 * mit ihren Standardwerten angelegt.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchuelerLernabschnittCreateRequest extends SchuelerLernabschnittsdatenPatchRequest {

	/** Die ID des Schülers, zu dem der Lernabschnitt angelegt wird. */
	@Schema(description = "Die ID des Schülers, zu dem der Lernabschnitt angelegt wird.", example = "4711")
	@NotNull
	public Long schuelerID;

	/** Die ID des Schuljahresabschnitts, zu dem der Lernabschnitt angelegt wird. */
	@Schema(description = "Die ID des Schuljahresabschnitts, zu dem der Lernabschnitt angelegt wird.", example = "42")
	@NotNull
	public Long schuljahresabschnitt;

}
