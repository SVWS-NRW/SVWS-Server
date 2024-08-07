package de.svws_nrw.core.utils.kataloge.vermerkart;

import de.svws_nrw.core.data.schule.VermerkartEintrag;
import jakarta.validation.constraints.NotNull;

import java.util.Comparator;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf VermerkArten zur Verfügung.
 */
public final class VermerkArtUtils {

	private VermerkArtUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/** Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten. */
	public static final @NotNull Comparator<VermerkartEintrag> comparator =
			(final @NotNull VermerkartEintrag a, final @NotNull VermerkartEintrag b) -> {
				int cmp = a.sortierung - b.sortierung;
				if (cmp != 0)
					return cmp;
				if ((a.bezeichnung == null) || (b.bezeichnung == null))
					return Long.compare(a.id, b.id);
				cmp = a.bezeichnung.compareTo(b.bezeichnung);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

}
