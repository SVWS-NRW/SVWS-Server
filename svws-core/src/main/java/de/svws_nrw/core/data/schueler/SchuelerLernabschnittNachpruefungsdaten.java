package de.svws_nrw.core.data.schueler;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse liefert die Informationen zu Nachprüfungen bei eines Lernabschnitts eines Schülers zurück.
 * Siehe auch {@link SchuelerLernabschnittsdaten}.
 */
@XmlRootElement
@Schema(description = "Die Informationen zu Nachprüfungen bei eines Lernabschnitts eines Schülers.")
@TranspilerDTO
public class SchuelerLernabschnittNachpruefungsdaten {

	/** Die Kürzel der möglichen Nachprüfungsfächer. */
	@ArraySchema(schema = @Schema(implementation = String.class, description = "Ein Array mit den Kürzeln der möglichen Nachprüfungsfächer."))
	public @NotNull List<@NotNull String> moegliche = new ArrayList<>();

	/** Die angesetzten bzw. durchgeführten Nachprüfungen */
	@ArraySchema(schema = @Schema(implementation = SchuelerLernabschnittNachpruefung.class, description = "Die angesetzten bzw. durchgeführten Nachprüfungen."))
	public @NotNull List<@NotNull SchuelerLernabschnittNachpruefung> pruefungen = new ArrayList<>();

}
