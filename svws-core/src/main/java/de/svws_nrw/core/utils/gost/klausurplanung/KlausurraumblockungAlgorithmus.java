package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Algorithmus der Klausuren (der Schüler) eines bestimmten Klausurtermins auf vorgegebene Räume blockt
 * und dabei bestimmte Regeln beachtet bzw. optimiert.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurraumblockungAlgorithmus {

	/**
	 * Verteilt die Klausuren auf die zur Verfügung stehenden Räume.
	 * <br>
	 * <br>Harte Kriterien:
	 * <br>- Die Raumkapazität darf nicht überschritten werden
	 * <br>- Es dürfen nur Klausuren in einen Raum geblockt werden, die dieselbe Startzeit haben.
	 * <br>
	 * <br>Weiche Kriterien (mit zugeordneter Güte von 0 ... 1):
	 * <br>- Möglichst geringe Raumanzahl.
	 * <br>- Möglichst gleiche Klausurlängen in einem Raum.
	 * <br>- Möglichst Klausuren des selben Kurses im selben Raum.
	 *
	 * @param config   		  Die Konfiguration und die Eingabedaten.
	 *
	 * @return Eine Liste von Paaren: 1. Element = GostKlausurraumRich (Nachschreiber), 2. Element = Liste von GostSchuelerklausurTerminRich-Objekten
	 *
	 */
	public @NotNull List<@NotNull GostKlausurraumRich> berechne(final @NotNull GostKlausurraumblockungKonfiguration config) {

		return new ArrayList<>();
	}


}
