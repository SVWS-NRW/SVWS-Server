package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten, die für eine Rückmeldung der Belegprüfungsergebnisse
 * für einen Abiturjahrgang der gymnasialen Oberstufe benötigt werden.
 */
@XmlRootElement
@Schema(description = "Die Rückmeldung der Belegprüfungsergebnisse einer Belegprüfung für einen Abiturjahrgang in der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostBelegpruefungsErgebnisse {

	/** Die Art der durchgeführten Belegprüfung. */
	@Schema(description = "die Art der durchgeführten Belegprüfung", implementation = String.class)
	public @NotNull String kuerzel = GostBelegpruefungsArt.GESAMT.kuerzel;

	/** Die Liste der Schüler, für welche die Belegprüfung durchgeführt wurde */
	@ArraySchema(schema = @Schema(implementation = Schueler.class))
	public @NotNull List<@NotNull Schueler> schueler = new ArrayList<>();

	/** Die Liste der Belegprüfungsergebnisse, die Anzahl und Reihenfolge muss der Anzahl und Reihenfolge der Schülerlist entsprechen. */
	@ArraySchema(schema = @Schema(implementation = GostBelegpruefungErgebnis.class))
	public @NotNull List<@NotNull GostBelegpruefungErgebnis> ergebnisse = new ArrayList<>();

}
