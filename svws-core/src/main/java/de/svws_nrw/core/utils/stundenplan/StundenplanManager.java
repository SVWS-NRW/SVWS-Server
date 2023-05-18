package de.svws_nrw.core.utils.stundenplan;

import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanManager {


	/**
	 * Der {@link StundenplanManager} benötigt vier data-Objekte und baut damit eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param daten                 liefert die Grunddaten des Stundenplanes.
	 * @param unterricht            liefert die Informationen zu einem Unterricht im Stundenplan.
	 * @param pausenaufsicht        liefert die Informationen zu den Pausenaufsichten im Stundenplan.
	 * @param unterrichtsverteilung liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans.
	 *
	 */
	public StundenplanManager(final @NotNull Stundenplan daten, final @NotNull StundenplanUnterricht unterricht, final @NotNull StundenplanPausenaufsicht pausenaufsicht, final @NotNull StundenplanUnterrichtsverteilung unterrichtsverteilung) {
		// ...
	}

}
