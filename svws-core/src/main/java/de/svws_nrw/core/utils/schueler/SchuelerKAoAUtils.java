package de.svws_nrw.core.utils.schueler;

import java.util.Comparator;

import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Schueler-KAoA zur Verfügung.
 */
public final class SchuelerKAoAUtils {

	private SchuelerKAoAUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von KAoA in KAoA-Listen. */
	public static final @NotNull Comparator<@NotNull SchuelerKAoADaten> comparator = (final @NotNull SchuelerKAoADaten a, final @NotNull SchuelerKAoADaten b) -> {
		int cmp = Long.compare(a.abschnitt, b.abschnitt);
		if (cmp != 0)
			return cmp;
		cmp = Long.compare(a.kategorie, b.kategorie);
		if (cmp != 0)
			return cmp;
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

}
