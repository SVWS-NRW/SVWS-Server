package de.svws_nrw.core.utils.kurse;

import java.util.Comparator;

import de.svws_nrw.core.data.kurse.KursListeEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Kurse zur Verfügung.
 */
public final class KursUtils {

	private KursUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten. */
	public static final @NotNull Comparator<@NotNull KursListeEintrag> comparator = (final @NotNull KursListeEintrag a, final @NotNull KursListeEintrag b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;
		cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

}
