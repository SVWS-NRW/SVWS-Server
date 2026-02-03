package de.svws_nrw.core.utils.bk;

import java.util.Comparator;

import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Abitur-Jahrgänge der gymnasialen Oberstufe zur Verfügung.
 */
public final class BKGymAbiturUtils {

	private BKGymAbiturUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Comparator für das DTO BKGymAbiturMarkierungsalgorithmusMarkierung
	 * Es wird nach höchster Punktzahl, FachID, Halbjahr verglichen, so dass
	 * die Verwendung des Comparators in sort zu einer eindeutigen Sortierreihenfolge führt.
	 */
	public static final @NotNull Comparator<BKGymAbiturMarkierungsalgorithmusMarkierung> comparatorMarkierung =
			(final @NotNull BKGymAbiturMarkierungsalgorithmusMarkierung a, final @NotNull BKGymAbiturMarkierungsalgorithmusMarkierung b) -> {
				if (b.punkte == null)
					return -1;
				if (a.punkte == null)
					return 1;
				final int tmp = b.punkte - a.punkte;
				if (tmp != 0)
					return tmp;
				// Ansonsten gilt die Sortierung des Faches ...
				final long ltmp = a.fachID - b.fachID;
				if (ltmp < 0)
					return -1;
				if (ltmp > 0)
					return 1;
				return a.halbjahrID - b.halbjahrID;
			};

	/**
	 * Comparator für das DTO BeruflichesGymnasiumStundentafelFach
	 * Es wird nach höchster Punktzahl, FachID, Halbjahr verglichen, so dass
	 * die Verwendung des Comparators in sort zu einer eindeutigen Sortierreihenfolge führt.
	 */
	public static final @NotNull Comparator<BeruflichesGymnasiumStundentafelFach> comparatorStundentafelFach =
			(final @NotNull BeruflichesGymnasiumStundentafelFach a, final @NotNull BeruflichesGymnasiumStundentafelFach b) -> {
				return a.sortierung - b.sortierung;
			};
}
