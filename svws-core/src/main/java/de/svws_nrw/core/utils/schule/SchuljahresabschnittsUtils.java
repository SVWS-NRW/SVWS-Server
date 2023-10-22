package de.svws_nrw.core.utils.schule;

import java.util.Comparator;

import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Schuljahresabschnitte zur Verfügung.
 */
public final class SchuljahresabschnittsUtils {

	private SchuljahresabschnittsUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von Schuljahresabschnitten in Schuljahresabschnittslisten. */
	public static final @NotNull Comparator<@NotNull Schuljahresabschnitt> comparator = (final @NotNull Schuljahresabschnitt a, final @NotNull Schuljahresabschnitt b) -> {

		int cmp = a.schuljahr - b.schuljahr;
		if (cmp != 0)
			return cmp;
		cmp = a.abschnitt - b.abschnitt;
		if (cmp != 0)
			return cmp;
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

}
