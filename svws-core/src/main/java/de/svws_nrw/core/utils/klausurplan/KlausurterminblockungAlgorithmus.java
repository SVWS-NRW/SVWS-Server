package de.svws_nrw.core.utils.klausurplan;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Algorithmus zur Blockung von Klausuren auf eine minimale Anzahl von Terminen.
 * Die Termine sind noch keinem Datum zugeordnet.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurterminblockungAlgorithmus {

	/**
	 * Der Konstruktor ist leer und erstellt auch keine Datenstrukturen.
	 */
	public KlausurterminblockungAlgorithmus() {
	}

	/**
	 * @param pInput   Die Eingabe beinhaltet alle Klausuren, welche die SuS beinhalten.
	 * @param pConfig  Das Konfigurationsobjekt für den Algorithmus.
	 * @return Eine Liste (Termine) von Listen (KlausurIDs)
	 */
	public @NotNull List<@NotNull List<@NotNull Long>> berechne(final @NotNull List<@NotNull GostKursklausur> pInput, final @NotNull KlausurterminblockungAlgorithmusConfig pConfig) {
		final @NotNull List<@NotNull List<@NotNull Long>> out = new Vector<>();
		switch (pConfig.get_lk_gk_modus()) {
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE:
				berechne_helper(pInput, pConfig, out);
				break;
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_LK:
				berechne_helper(filter(pInput, true), pConfig, out);  // nur LK
				break;
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_GK:
				berechne_helper(filter(pInput, false), pConfig, out);  // nur GK
				break;
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_GETRENNT:
				berechne_helper(filter(pInput, true), pConfig, out);  // nur LK
				berechne_helper(filter(pInput, false), pConfig, out);  // nur GK
				break;
			default:
				throw new DeveloperNotificationException("Der LK-GK-Modus ist unbekannt!");
		}
		return out;
	}

	private static @NotNull List<@NotNull GostKursklausur> filter(final @NotNull List<@NotNull GostKursklausur> pInput, final boolean pLK) {
		final @NotNull List<@NotNull GostKursklausur> temp = new Vector<>();
		for (final GostKursklausur gostKursklausur : pInput)
			if (gostKursklausur.kursart.equals("LK") == pLK)
				temp.add(gostKursklausur);
		return temp;
	}

	private static void berechne_helper(final @NotNull List<@NotNull GostKursklausur> pInput, final @NotNull KlausurterminblockungAlgorithmusConfig pConfig, final @NotNull List<@NotNull List<@NotNull Long>> out) {
		// End-Zeitpunkt berechnet.
		final long zeitEndeGesamt = System.currentTimeMillis() + pConfig.get_max_time_millis();

		// Random-Objekt erzeugen
		final long seed = new Random().nextLong(); // Trick, um den Seed zu kennen.
		final @NotNull Random random = new Random(seed);

		// Erstellung eines Objektes, welches sich um alle dynamischen Daten während des Blockungsvorgangs kümmert.
		final KlausurterminblockungDynDaten dynDaten = new KlausurterminblockungDynDaten(random, pInput, pConfig);

		// Algorithmen erzeugen
		final @NotNull KlausurterminblockungAlgorithmusAbstract @NotNull [] algorithmen = new KlausurterminblockungAlgorithmusAbstract @NotNull [] {
				// Alle Algorithmen zur Verteilung von Klausuren auf ihre Termine ...
				new KlausurterminblockungAlgorithmusGreedy1(random, dynDaten), // Klausurgruppen zufällig, Termine zufällig
				new KlausurterminblockungAlgorithmusGreedy1b(random, dynDaten), // Termine nacheinander, Klausurgruppen zufällig
				new KlausurterminblockungAlgorithmusGreedy2(random, dynDaten), // Klausurgruppen nach Grad, Termine zufällig
				new KlausurterminblockungAlgorithmusGreedy2b(random, dynDaten), // Termine nacheinander, Klausurgruppen nach Grad
				new KlausurterminblockungAlgorithmusGreedy3(random, dynDaten), // Termine nacheinander, Klausurgruppen mit Backtracking
				// ... Ende der Algorithmen.
		};


		// Generiere das erste Ergebnis (Zustand2 = bisheriges globales Optimum)
		dynDaten.aktion_Clear_TermineNacheinander_GruppeZufaellig();
		dynDaten.aktionZustand2Speichern();

		long zeitProAlgorithmus = 10L; // Weniger ist nicht gut.
		do {
			// System.out.println("\nzeitProAlgorithmus --> " + zeitProAlgorithmus);

			// Alle Algorithmus starten (pro Algorithmus ggf. auch mehrfach).
			for (int iAlgo = 0; iAlgo < algorithmen.length; iAlgo++) {
				final long zeitEndeRunde = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmen[iAlgo].berechne(zeitEndeRunde);
				// System.out.println(algorithmen[iAlgo].toString()+" --> " + dynDaten.gibTerminAnzahl() );
			}

			zeitProAlgorithmus *= 2; // Nächste Runde hat mehr Zeit.
		} while (System.currentTimeMillis() + algorithmen.length * zeitProAlgorithmus <= zeitEndeGesamt); // noch Zeit?

		// Lade besten globalen Zustand
		dynDaten.aktionZustand2Laden();

		out.addAll(dynDaten.gibErzeugeOutput());
	}

}
