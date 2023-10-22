package de.svws_nrw.core.utils.klassen;

import java.util.Comparator;

import de.svws_nrw.core.data.klassen.KlassenListeEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Klassen zur Verfügung.
 */
public final class KlassenUtils {

	private KlassenUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten. */
	public static final @NotNull Comparator<@NotNull KlassenListeEintrag> comparator = (final @NotNull KlassenListeEintrag a, final @NotNull KlassenListeEintrag b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;
		if ((a.kuerzel == null) || (b.kuerzel == null))
			return Long.compare(a.id, b.id);
		cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};


}
