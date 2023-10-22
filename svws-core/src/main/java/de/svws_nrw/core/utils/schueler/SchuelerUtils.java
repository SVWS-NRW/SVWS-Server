package de.svws_nrw.core.utils.schueler;

import java.util.Comparator;

import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Schüler zur Verfügung.
 */
public final class SchuelerUtils {

	private SchuelerUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von Schülern in Schuelerlisten. */
	public static final @NotNull Comparator<@NotNull SchuelerListeEintrag> comparator = (final @NotNull SchuelerListeEintrag a, final @NotNull SchuelerListeEintrag b) -> {
		int cmp = a.nachname.compareTo(b.nachname);
		if (cmp != 0)
			return cmp;
		cmp = a.vorname.compareTo(b.vorname);
		if (cmp != 0)
			return cmp;
		cmp = a.jahrgang.compareTo(b.jahrgang);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};


}
