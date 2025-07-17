package de.svws_nrw.core.abschluss.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Eine Klasse für die Rückmeldung zu einer Abiturmarkierungspruefung in der gymnasialen Oberstufe.
 */
@XmlRootElement(name = "GostAbiturMarkierungspruefungErgebnis")
@Schema(name = "GostAbiturMarkierungspruefungErgebnis", description = "enthält die Informationen zu dem Ergebnis der Abiturmarkierungsprüfung")
@TranspilerDTO
public class GostAbiturMarkierungspruefungErgebnis {

	/** gibt an, ob die Prüfung erfolgreich war */
	@Schema(description = "gibt an, ob die Prüfung erfolgreich war.", example = "true")
	public boolean erfolgreich = false;

	/** Ein Log, der den Ablauf der Markierungsprüfung verdeutlicht */
	@ArraySchema(schema = @Schema(description = "der Log der Markierungsprüfung."))
	public @NotNull List<String> log = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostAbiturMarkierungspruefungErgebnis() {
		// leer
	}

}
