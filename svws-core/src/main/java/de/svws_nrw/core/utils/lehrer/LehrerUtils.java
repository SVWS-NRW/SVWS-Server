package de.svws_nrw.core.utils.lehrer;

import java.util.Comparator;

import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Lehrer zur Verfügung.
 */
public final class LehrerUtils {

	private LehrerUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von Lehrern in Lehrerlisten. */
	public static final @NotNull Comparator<@NotNull LehrerListeEintrag> comparator = (final @NotNull LehrerListeEintrag a, final @NotNull LehrerListeEintrag b) -> {
		int cmp = a.nachname.compareTo(b.nachname);
		if (cmp != 0)
			return cmp;
		cmp = a.vorname.compareTo(b.vorname);
		if (cmp != 0)
			return cmp;
		cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

}
