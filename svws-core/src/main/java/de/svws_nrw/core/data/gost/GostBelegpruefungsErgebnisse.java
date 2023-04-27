package de.svws_nrw.core.data.gost;

import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten, die für eine Rückmeldung der Belegprüfungsergebnisse
 * für einen Schüler der gymnasialen Oberstufe benötigt werden.
 */
@XmlRootElement
@Schema(description = "Die Rückmeldung der Belegprüfungsergebnisse einer Belegprüfung für einen Schüler in der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostBelegpruefungsErgebnisse {

	/** Der Schüler, für welchen die Belegprüfung durchgeführt wurde */
	@Schema(implementation = Schueler.class)
	public @NotNull Schueler schueler = new Schueler();

	/** Die zugehörigen Belegprüfungsergebnisse */
	@Schema(implementation = GostBelegpruefungErgebnis.class)
	public @NotNull GostBelegpruefungErgebnis ergebnis = new GostBelegpruefungErgebnis();

}
