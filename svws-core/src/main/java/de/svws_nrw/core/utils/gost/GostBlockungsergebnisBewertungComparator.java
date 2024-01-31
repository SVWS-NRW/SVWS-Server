package de.svws_nrw.core.utils.gost;

import java.util.Comparator;

import de.svws_nrw.core.data.gost.GostBlockungsergebnisBewertung;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Klasse dient der GUI dazu Klassen des Typs {@link GostBlockungsergebnisBewertung} zu vergleichen.<br>
 */
public final class GostBlockungsergebnisBewertungComparator implements Comparator<@NotNull GostBlockungsergebnisBewertung> {

	/**
	 * Erzeugt einen Comparator für zwei Objekte des Typs {@link GostBlockungsergebnisBewertung}.
	 * Zwei Elemente werden nach folgender Priorität sortiert:
	 * <br>(1) Array an Regelverletzungen (weniger besser) + Anzahl nicht gesetzter Kurse
	 * <br>(2) Summe nicht zugeordneter Fachwahlen + Summe an Kollisionen (weniger besser)
	 * <br>(3) Array an Kursdifferenzen-Histogramm (kleinste größte Kursdifferenz besser)
	 * <br>(4) Summe gleicher Facharten in der selben Schiene (weniger besser) <br>
	 */
	public GostBlockungsergebnisBewertungComparator() {
		// empty constructor
	}

	@Override
	public int compare(final @NotNull GostBlockungsergebnisBewertung o1, final @NotNull GostBlockungsergebnisBewertung o2) {

		// Bewertungskriterium 1: Je weniger nicht erfüllter Regeln, desto besser.
		final int o1Wert1 = o1.regelVerletzungen.size() + o1.anzahlKurseNichtZugeordnet;
		final int o2Wert1 = o2.regelVerletzungen.size() + o2.anzahlKurseNichtZugeordnet;
		if (o1Wert1 < o2Wert1) return -1;
		if (o1Wert1 > o2Wert1) return +1;

		// Bewertungskriterium 2: Je weniger nicht zugeordnete Fachwahlen und Kollisionen, desto besser.
		final int o1Wert2 = o1.anzahlSchuelerNichtZugeordnet + o1.anzahlSchuelerKollisionen;
		final int o2Wert2 = o2.anzahlSchuelerNichtZugeordnet + o2.anzahlSchuelerKollisionen;
		if (o1Wert2 < o2Wert2) return -1;
		if (o1Wert2 > o2Wert2) return +1;

		// Bewertungskriterium 3: Je kleiner die Größte Kursdifferenz, desto besser.
		final int kdMax1 = o1.kursdifferenzMax;
		final int kdMax2 = o2.kursdifferenzMax;
		if (kdMax1 < kdMax2) return -1;
		if (kdMax1 > kdMax2) return +1;

		final int[] o1Kursdifferenzen = o1.kursdifferenzHistogramm;
		final int[] o2Kursdifferenzen = o2.kursdifferenzHistogramm;
		for (int i = kdMax1; i >= 0; i--) {
			if (o1Kursdifferenzen[i] < o2Kursdifferenzen[i]) return -1;
			if (o1Kursdifferenzen[i] > o2Kursdifferenzen[i]) return +1;
		}

		// Bewertungskriterium 4: Je weniger Facharten in der selben Schiene sind, desto besser.
		final int o1Wert4 = o1.anzahlKurseMitGleicherFachartProSchiene;
		final int o2Wert4 = o2.anzahlKurseMitGleicherFachartProSchiene;
		if (o1Wert4 < o2Wert4) return -1;
		if (o1Wert4 > o2Wert4) return +1;

		// Identische Bewertung.
		return 0;
	}
}
