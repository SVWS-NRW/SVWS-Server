package de.svws_nrw.core.utils.gost;

import java.util.Comparator;

import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisBewertung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisListeneintrag;
import jakarta.validation.constraints.NotNull;

/** Dieser Klasse dient der GUI dazu, Blockungsergebnisse zu sortieren. <br>
 * Die GUI erhält von der Datenbank eine Liste von {@link GostBlockungsergebnisListeneintrag}, sobald man auf
 * ein Element der Liste klickt, wird ein {@link GostBlockungsergebnis} Objekt geladen. */
public class GostBlockungsergebnisComparator implements Comparator<@NotNull GostBlockungsergebnisListeneintrag> {

	/** Erzeugt einen Comparator für zwei Objekte des Typs {@link GostBlockungsergebnisListeneintrag}. Zwei
	 * Elemente werden nach folgender Priorität sortiert: <br>
	 * (1) Array an Regelverletzungen (weniger besser) + Anzahl nicht gesetzter Kurse<br>
	 * (2) Summe nicht zugeordneter Fachwahlen + Summe an Kollisionen (weniger besser) <br>
	 * (3) Array an Kursdifferenzen-Histogramm (kleinste größte Kursdifferenz besser) <br>
	 * (4) Summe gleicher Facharten in der selben Schiene (weniger besser) <br>
	 * (5) ID von GostBlockungsergebnisListeneintrag (weniger besser) <br>
	 */
	public GostBlockungsergebnisComparator() {
	}

	@Override
	public int compare(final @NotNull GostBlockungsergebnisListeneintrag o1, final @NotNull GostBlockungsergebnisListeneintrag o2) {
		final @NotNull GostBlockungsergebnisBewertung b1 = o1.bewertung; 
		final @NotNull GostBlockungsergebnisBewertung b2 = o2.bewertung;
		
		// Bewertungskriterium 1: Je weniger nicht erfüllter Regeln, desto besser.
		final int o1Wert1 = b1.regelVerletzungen.size() + b1.anzahlKurseNichtZugeordnet;
		final int o2Wert1 = b2.regelVerletzungen.size() + b2.anzahlKurseNichtZugeordnet;
		if (o1Wert1 < o2Wert1) return -1;
		if (o1Wert1 > o2Wert1) return +1;

		// Bewertungskriterium 2: Je weniger nicht zugeordnete Fachwahlen und Kollisionen, desto besser.
		final int o1Wert2 = b1.anzahlSchuelerNichtZugeordnet + b1.anzahlSchuelerKollisionen;
		final int o2Wert2 = b2.anzahlSchuelerNichtZugeordnet + b2.anzahlSchuelerKollisionen;
		if (o1Wert2 < o2Wert2) return -1;
		if (o1Wert2 > o2Wert2) return +1;

		// Bewertungskriterium 3: Je kleiner die Größte Kursdifferenz, desto besser.
		final int kdMax1 = b1.kursdifferenzMax;
		final int kdMax2 = b2.kursdifferenzMax;
		if (kdMax1 < kdMax2) return -1;
		if (kdMax1 > kdMax2) return +1;
		
		final int[] o1Kursdifferenzen = b1.kursdifferenzHistogramm;
		final int[] o2Kursdifferenzen = b2.kursdifferenzHistogramm;
		for (int i = kdMax1; i >= 0; i--) {
			if (o1Kursdifferenzen[i] < o2Kursdifferenzen[i]) return -1;
			if (o1Kursdifferenzen[i] > o2Kursdifferenzen[i]) return +1;
		}

		// Bewertungskriterium 4: Je weniger Facharten in der selben Schiene sind, desto besser.
		final int o1Wert4 = b1.anzahlKurseMitGleicherFachartProSchiene;
		final int o2Wert4 = b2.anzahlKurseMitGleicherFachartProSchiene;
		if (o1Wert4 < o2Wert4) return -1;
		if (o1Wert4 > o2Wert4) return +1;

		// Bewertungskriterium 5: Bei absolut identischen Ergebnissen gewinnt die kleinere ID des Zwischenergebnisses.
		if (o1.id < o2.id) return -1;
		if (o1.id > o2.id) return +1;

		return 0; // Sollte niemals, da die IDs niemals gleich sind.
	}
}