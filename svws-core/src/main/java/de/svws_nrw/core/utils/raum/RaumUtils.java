package de.svws_nrw.core.utils.raum;

import java.util.Comparator;

import de.svws_nrw.core.data.schule.Raum;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Fächer zur Verfügung.
 */
public final class RaumUtils {

	private RaumUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von Räumen in Raumlisten. */
	public static final @NotNull Comparator<Raum> comparator = (final @NotNull Raum a, final @NotNull Raum b) -> {
		int cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

}
