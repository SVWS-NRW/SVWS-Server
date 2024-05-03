package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTerminRich;
import jakarta.validation.constraints.NotNull;

public class KlausurraumblockungAlgorithmus {

	/**
	 * @param config   		  Die Konfiguration
	 *
	 * @return Eine Liste von Paaren: 1. Element = GostKlausurraumRich (Nachschreiber), 2. Element = Liste von GostSchuelerklausurTerminRich-Objekten
	 *
	 * Harte Parameter sind:
	 * - Die Raumkapazität darf nicht überschritten werden
	 * - Es dürfen nur Klausuren in einen Raum geblockt werden, die dieselbe Startzeit haben.
	 */
	public @NotNull List<@NotNull Pair<@NotNull GostKlausurraumRich, @NotNull List<GostSchuelerklausurTerminRich>>> berechne(
					final @NotNull GostKlausurraumblockungKonfiguration config) {

		return new ArrayList<>();
	}


}
