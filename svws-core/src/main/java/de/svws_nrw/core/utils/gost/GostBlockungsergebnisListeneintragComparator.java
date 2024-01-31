package de.svws_nrw.core.utils.gost;

import java.util.Comparator;

import de.svws_nrw.core.data.gost.GostBlockungsergebnisListeneintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Klasse dient dazu Objekte des Typs {@link GostBlockungsergebnisListeneintrag} zu sortieren.
 */
public final class GostBlockungsergebnisListeneintragComparator implements Comparator<@NotNull GostBlockungsergebnisListeneintrag> {

	/**
	 * Erzeugt einen Comparator für zwei Objekte des Typs {@link GostBlockungsergebnisListeneintrag}.
	 * Zwei Elemente werden nach folgender Priorität sortiert:
	 * <br>(1) Array an Regelverletzungen (weniger besser) + Anzahl nicht gesetzter Kurse
	 * <br>(2) Summe nicht zugeordneter Fachwahlen + Summe an Kollisionen (weniger besser)
	 * <br>(3) Array an Kursdifferenzen-Histogramm (kleinste größte Kursdifferenz besser)
	 * <br>(4) Summe gleicher Facharten in der selben Schiene (weniger besser)
	 * <br>(5) ID von GostBlockungsergebnisListeneintrag (weniger besser)
	 */
	public GostBlockungsergebnisListeneintragComparator() {
		// empty constructor
	}

	@Override
	public int compare(final @NotNull GostBlockungsergebnisListeneintrag o1, final @NotNull GostBlockungsergebnisListeneintrag o2) {
		// Bewertungskriterium 1-4:
		final int cmp = GostBlockungsergebnisBewertungComparator.compareBewertungen(o1.bewertung, o2.bewertung);
		if (cmp != 0)
			return cmp;

		// Bewertungskriterium 5: Bei absolut identischen Ergebnissen gewinnt die kleinere ID des Zwischenergebnisses.
		if (o1.id < o2.id) return -1;
		if (o1.id > o2.id) return +1;

		return 0; // Sollte niemals, da die IDs niemals gleich sind.
	}

}
