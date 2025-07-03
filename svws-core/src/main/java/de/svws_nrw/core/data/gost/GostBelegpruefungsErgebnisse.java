package de.svws_nrw.core.data.gost;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.transpiler.TranspilerDTO;
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

	/** Gibt an, ob der Schüler aktuell Fachwahlen hat der nicht. */
	@Schema(description = "gibt an, ob der Schüler aktuell Fachwahlen hat der nicht", example = "true")
	public boolean hatFachwahlen = false;

	/** Gibt an, ob und wann der Schüler zuletzt beraten wurde. */
	@Schema(description = "gibt an, ob und wann der Schüler zuletzt beraten wurde", example = "30.01.2042")
	public String beratungsDatum = null;

	/** Gibt an, ob und wann für den Schüler zuletzt ein Import der Laufbahnplanungsdaten stattgefunden hat. */
	@Schema(description = "gibt an, ob und wann für den Schüler zuletzt ein Import der Laufbahnplanungsdaten stattgefunden hat", example = "01.02.2042")
	public String ruecklaufDatum = null;

	/** Die zugehörigen Belegprüfungsergebnisse */
	@Schema(implementation = GostBelegpruefungErgebnis.class)
	public @NotNull GostBelegpruefungErgebnis ergebnis = new GostBelegpruefungErgebnis();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostBelegpruefungsErgebnisse() {
		// leer
	}

}
