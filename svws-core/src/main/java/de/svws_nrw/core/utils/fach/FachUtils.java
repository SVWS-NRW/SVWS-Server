package de.svws_nrw.core.utils.fach;

import java.util.Comparator;

import de.svws_nrw.core.data.fach.FachDaten;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Fächer zur Verfügung.
 */
public final class FachUtils {

	private FachUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von Fächern in Fächerlisten. */
	public static final @NotNull Comparator<FachDaten> comparator = (final @NotNull FachDaten a, final @NotNull FachDaten b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;
		cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

}
