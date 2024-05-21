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


    /** Ein Default-Comparator für den Vergleich von Religionen in Religionslisten. */
    public static final @NotNull Comparator<@NotNull ReligionEintrag> comparator = (final @NotNull ReligionEintrag a, final @NotNull ReligionEintrag b) -> {
        if ((a.kuerzel == null) || (b.kuerzel == null)) {
            if ((a.kuerzel == null) && (b.kuerzel == null))
                return 0;
            return (a.kuerzel == null) ? -1 : 1;
        }
        int cmp = a.kuerzel.compareTo(b.kuerzel);
        return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
    };

}
