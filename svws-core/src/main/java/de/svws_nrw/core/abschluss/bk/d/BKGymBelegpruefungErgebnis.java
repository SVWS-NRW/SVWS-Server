package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Eine Klasse für die Rückmeldung eines Belegprüfungsergebnis in der gymnasialen Oberstufe.
 */
@XmlRootElement(name = "GostBelegpruefungErgebnis")
@Schema(name = "GostBelegpruefungErgebnis", description = "gibt die Informationen zu dem Ergebnis der Belegprüfung an.")
@TranspilerDTO
public class BKGymBelegpruefungErgebnis {

	/** gibt an, ob die Belegprüfung erfolgreich abgeschlossen wurde */
	@Schema(description = "gibt an, ob die Belegprüfung erfolgreich abgeschlossen wurde.", example = "true")
	public boolean erfolgreich;

	/** eine Liste der Belegungsfehler und Hinweise zur Belegung */
	@ArraySchema(schema = @Schema(implementation = BKGymBelegpruefungErgebnisFehler.class,
			description = "eine Liste der Belegungsfehler und Hinweise zur Belegung."))
	public @NotNull List<BKGymBelegpruefungErgebnisFehler> fehlercodes = new ArrayList<>();

	/** Ein Log, der den Ablauf der Belegprüfung verdeutlicht */
	@ArraySchema(schema = @Schema(description = "der Log der Belegprüfung.", example = "Ein Log, der den Ablauf der Belegprüfung verdeutlicht"))
	public @NotNull List<String> log = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BKGymBelegpruefungErgebnis() {
		// leer
	}

}
