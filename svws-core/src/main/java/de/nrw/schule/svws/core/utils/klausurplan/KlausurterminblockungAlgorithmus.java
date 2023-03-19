package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.klausuren.GostKursklausur;
import de.nrw.schule.svws.core.exceptions.DeveloperNotificationException;
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
	public @NotNull List<@NotNull List<@NotNull Long>> berechne(@NotNull List<@NotNull GostKursklausur> pInput, @NotNull KlausurterminblockungAlgorithmusConfig pConfig) {
		
		@NotNull List<@NotNull List<@NotNull Long>> out = new Vector<>();
		
		switch (pConfig.get_lk_gk_modus()) {
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE: {
				berechne_helper(pInput, pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_LK: {
				@NotNull List<@NotNull GostKursklausur> nurLK = filter(pInput, true);
				berechne_helper(nurLK, pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_GK: {
				@NotNull List<@NotNull GostKursklausur> nurGK = filter(pInput, false);
				berechne_helper(nurGK, pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_GETRENNT: {
				@NotNull List<@NotNull GostKursklausur> nurLK = filter(pInput, true);
				@NotNull List<@NotNull GostKursklausur> nurGK = filter(pInput, false);
				berechne_helper(nurLK, pConfig, out);
				berechne_helper(nurGK, pConfig, out);
				break;
			}
			default: {
				throw new DeveloperNotificationException("Der LK-GK-Modus ist unbekannt!");
			}
		}
		
		return out;
	}

	private static @NotNull List<@NotNull GostKursklausur> filter(@NotNull List<@NotNull GostKursklausur> pInput, boolean pLK) {
		@NotNull List<@NotNull GostKursklausur> temp = new Vector<>();
		for (GostKursklausur gostKursklausur : pInput) 
			if (gostKursklausur.kursart.equals("LK") == pLK) 
				temp.add(gostKursklausur);
		return temp;
	}

	private static void berechne_helper(@NotNull List<@NotNull GostKursklausur> pInput, @NotNull KlausurterminblockungAlgorithmusConfig pConfig, @NotNull List<@NotNull List<@NotNull Long>> out) {
		// End-Zeitpunkt berechnet.
		long zeitEndeGesamt = System.currentTimeMillis() + pConfig.get_max_time_millis();

		// Random-Objekt erzeugen
		long seed = new Random().nextLong(); // Trick, um den Seed zu kennen.
		@NotNull Random random = new Random(seed);

		// Erstellung eines Objektes, welches sich um alle dynamischen Daten während des Blockungsvorgangs kümmert.
		KlausurterminblockungDynDaten dynDaten = new KlausurterminblockungDynDaten(random, pInput, pConfig);

		// Algorithmen erzeugen
		@NotNull KlausurterminblockungAlgorithmusAbstract @NotNull [] algorithmen = new KlausurterminblockungAlgorithmusAbstract @NotNull [] {
				// Alle Algorithmen zur Verteilung von Klausuren auf ihre Termine ...
				//new KlausurterminblockungAlgorithmusGreedy1(random, dynDaten), // Klausuren zufällig, Termine zufällig
				//new KlausurterminblockungAlgorithmusGreedy1b(random, dynDaten), // Termine nacheinander, Klausuren zufällig
				new KlausurterminblockungAlgorithmusGreedy2(random, dynDaten), // Klausuren nach Grad, Termine zufällig
				// ... Ende der Algorithmen.
		};

		
		// Generiere das erste Ergebnis (Zustand2 = bisheriges globales Optimum)
		dynDaten.aktion_Clear_TermineNacheinander_KlausurenZufaellig();
		dynDaten.aktionZustand2Speichern();
		
		long zeitProAlgorithmus = 10L; // Weniger ist nicht gut.
		do {
			// System.out.println("zeitProAlgorithmus --> " + zeitProAlgorithmus);

			// Alle Algorithmus starten (pro Algorithmus ggf. auch mehrfach).
			for (int iAlgo = 0; iAlgo < algorithmen.length; iAlgo++) {
				long zeitEndeRunde = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmen[iAlgo].berechne(zeitEndeRunde);
			}

			zeitProAlgorithmus *= 2; // Nächste Runde hat mehr Zeit.
		} while (System.currentTimeMillis() + algorithmen.length * zeitProAlgorithmus <= zeitEndeGesamt); // noch Zeit?
		
		// Lade besten globalen Zustand
		dynDaten.aktionZustand2Laden();

		out.addAll(dynDaten.gibErzeugeOutput());
	}

}
