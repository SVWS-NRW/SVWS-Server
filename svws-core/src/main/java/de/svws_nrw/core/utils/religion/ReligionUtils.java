package de.svws_nrw.core.utils.religion;

import java.util.Comparator;

import de.svws_nrw.core.data.schule.ReligionEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Religionen zur Verfügung.
 */
public final class ReligionUtils {

	private ReligionUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von Religion-Einträgen. */
	public static final @NotNull Comparator<ReligionEintrag> comparator =
			(final @NotNull ReligionEintrag a, final @NotNull ReligionEintrag b) -> {
				int cmp = a.sortierung - b.sortierung;
				if (cmp != 0)
					return cmp;
				if ((a.kuerzel == null) || (b.kuerzel == null)) {
					if ((a.kuerzel == null) && (b.kuerzel == null))
						return 0;
					return (a.kuerzel == null) ? -1 : 1;
				}
				cmp = a.kuerzel.compareTo(b.kuerzel);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	/** Ein Comparator für den Vergleich von Religion-Einträgen anhand ihres Textes. */
	public static final @NotNull Comparator<ReligionEintrag> comparatorText =
			(final @NotNull ReligionEintrag a, final @NotNull ReligionEintrag b) -> {
				if ((a.text == null) || (b.text == null)) {
					if ((a.text == null) && (b.text == null))
						return 0;
					return (a.text == null) ? -1 : 1;
				}
				final int cmp = a.text.compareTo(b.text);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

}
