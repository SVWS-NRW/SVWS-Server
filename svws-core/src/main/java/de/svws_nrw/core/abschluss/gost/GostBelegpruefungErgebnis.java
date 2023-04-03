package de.svws_nrw.core.abschluss.gost;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Eine Klasse für die Rückmeldung eines Belegprüfungsergebnis in der gymnasialen Oberstufe.
 */
@XmlRootElement(name = "GostBelegpruefungErgebnis")
@Schema(name = "GostBelegpruefungErgebnis", description = "gibt die Informationen zu dem Ergebnis der Belegprüfung an.")
@TranspilerDTO
public class GostBelegpruefungErgebnis {

	/** gibt an, ob die Belegprüfung erfolgreich abgeschlossen wurde */
	@Schema(required = true, description = "gibt an, ob die Belegprüfung erfolgreich abgeschlossen wurde.", example = "true")
	public boolean erfolgreich;

	/** eine Liste der Belegungsfehler und Hinweise zur Belegung */
	@ArraySchema(schema = @Schema(required = true, implementation = GostBelegpruefungErgebnisFehler.class, description = "eine Liste der Belegungsfehler und Hinweise zur Belegung."))
	public @NotNull Vector<@NotNull GostBelegpruefungErgebnisFehler> fehlercodes = new Vector<>();

	/** Ein Log, der den Ablauf der Belegprüfung verdeutlicht */
	@ArraySchema(schema = @Schema(required = false, description = "der Log der Belegprüfung.", example = "Ein Log, der den Ablauf der Belegprüfung verdeutlicht"))
	public @NotNull List<@NotNull String> log = new Vector<>();

}
