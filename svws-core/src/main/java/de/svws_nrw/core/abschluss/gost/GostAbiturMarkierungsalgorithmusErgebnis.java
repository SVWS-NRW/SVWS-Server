package de.svws_nrw.core.abschluss.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Eine Klasse für die Rückmeldung eines Ergebnisses des Abiturmarkierungsergebnis in der gymnasialen Oberstufe.
 */
@XmlRootElement(name = "GostAbiturMarkierungsalgorithmusErgebnis")
@Schema(name = "GostAbiturMarkierungsalgorithmusErgebnis", description = "enthält die Informationen zu dem Ergebnis des Abiturmarkierungsergebnis")
@TranspilerDTO
public class GostAbiturMarkierungsalgorithmusErgebnis {

	/** gibt an, ob der Algorithmus erfolgreich durchgeführt wurde */
	@Schema(description = "gibt an, ob der Algorithmus erfolgreich durchgeführt wurde.", example = "true")
	public boolean erfolgreich = false;

	/** eine Liste der vorgenommenen Markierungen von Halbjahres-Belegungen in der Qualifikationsphase */
	@ArraySchema(schema = @Schema(implementation = GostAbiturMarkierungsalgorithmusMarkierung.class,
			description = "eine Liste der vorgenommenen Markierungen von Halbjahres-Belegungen in der Qualifikationsphase."))
	public @NotNull List<GostAbiturMarkierungsalgorithmusMarkierung> markierungen = new ArrayList<>();

	/** Ein Log, der den Ablauf des Markierungsalgorithmus verdeutlicht */
	@ArraySchema(schema = @Schema(description = "der Log des Markierungsalgorithmus.", example = "Ein Log, der den Ablauf des Markierungsalgorithmus verdeutlicht"))
	public @NotNull List<String> log = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostAbiturMarkierungsalgorithmusErgebnis() {
		// leer
	}

}
