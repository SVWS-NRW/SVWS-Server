package de.svws_nrw.core.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse liefert die Informationen zu einer Nachprüfung eines Schülers zurück.
 * Siehe auch {@link SchuelerLernabschnittsdaten}.
 */
@XmlRootElement
@Schema(description="Die Informationen zu einer Nachprüfung eines Schülers.")
@TranspilerDTO
public class SchuelerLernabschnittNachpruefung {
	
	/** Der Grund für die Versetzung (V - Versetzung, A - Abschluss, B - berufsqualifizierend) */
	@Schema(required = true, description = "der Grund für die Versetzung (V - Versetzung, A - Abschluss, B - berufsqualifizierend)", example="V")
	public @NotNull String grund = "V"; 	

	/** Die ID des Faches, auf welches sich die Leistungsdaten beziehen. */
	@Schema(required = true, description = "die ID des Lernabschnitts des Schülers, zu dem diese Leistungsdaten gehören", example="46")
	public long fachID = -1;

	/** Das Kürzel der Note in der Nachprüfung. */
	@Schema(required = true, description = "das Kürzel der Note in der Nachprüfung", example="4")	
	public String note = null;

	/** Das Datum der Nachprüfung. */
	@Schema(required = true, description = "das Datum der Nachprüfung", example="4")
	public String datum = null;

}
