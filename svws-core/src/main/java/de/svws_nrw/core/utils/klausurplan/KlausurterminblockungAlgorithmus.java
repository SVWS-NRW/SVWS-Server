package de.svws_nrw.core.utils.klausurplan;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Algorithmus zur Blockung von Klausuren auf eine minimale Anzahl von Terminen.
 * Die Termine sind noch keinem Datum zugeordnet.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurterminblockungAlgorithmus {

	private static final @NotNull Random _random = new Random();

	/** Ein Logger für Debug-Zwecke. */
	private final @NotNull Logger _logger;

	/**
	 * Der Konstruktor.
	 */
	public KlausurterminblockungAlgorithmus() {
		_logger = new Logger();
	}

	/**
	 * Der Konstruktor.
	 *
	 * @param pLogger  Ein Logger für Debug-Zwecke.
	 */
	public KlausurterminblockungAlgorithmus(final @NotNull Logger pLogger) {
		_logger = pLogger;
	}

	/**
	 * Liefert eine Liste (Termine, Ebene 1) von Listen (KlausurIDs, Ebene 2).
	 *
	 * @param pInput   Die Eingabe beinhaltet alle Klausuren, welche die SuS beinhalten.
	 * @param pConfig  Das Konfigurationsobjekt für den Algorithmus.
	 *
	 * @return eine Liste (Termine, Ebene 1) von Listen (KlausurIDs, Ebene 2).
	 */
	public @NotNull List<@NotNull List<@NotNull Long>> berechne(final @NotNull List<@NotNull GostKursklausur> pInput, final @NotNull KlausurterminblockungAlgorithmusConfig pConfig) {
		final @NotNull List<@NotNull List<@NotNull Long>> out = new ArrayList<>();

		// Der LK-GK-Modus bestimmt, welche Klausuren aus der Liste pInput potentiell herausgefiltert werden müssen.
		switch (pConfig.get_lk_gk_modus()) {
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE:
				berechne_helper(pInput, pConfig, out); // keine Filterung
				break;
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_LK:
				berechne_helper(filter(pInput, true), pConfig, out);  // nur LK
				break;
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_GK:
				berechne_helper(filter(pInput, false), pConfig, out);  // nur GK (bzw. nicht LK)
				break;
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_GETRENNT:
				berechne_helper(filter(pInput, true), pConfig, out);  // nur LK
				berechne_helper(filter(pInput, false), pConfig, out);  // nur GK (bzw. nicht LK)
				break;
			default:
				throw new DeveloperNotificationException("Der LK-GK-Modus ist unbekannt!");
		}

		return out;
	}

	/**
	 * Liefert die Liste pInput nach LK-Klausuren (oder dem Gegenteil) gefiltert heraus.
	 *
	 * @param pInput Die Liste, die gefiltert werden soll.
	 * @param pLK    Falls TRUE, werden die LK-Klausuren herausgefiltert, andernfalls das Gegenteil.
	 *
	 * @return die Liste pInput nach LK-Klausuren (oder dem Gegenteil) gefiltert heraus.
	 */
	private static @NotNull List<@NotNull GostKursklausur> filter(final @NotNull List<@NotNull GostKursklausur> pInput, final boolean pLK) {
		final @NotNull List<@NotNull GostKursklausur> temp = new ArrayList<>();

		for (final GostKursklausur gostKursklausur : pInput)
			if (gostKursklausur.kursart.equals("LK") == pLK)
				temp.add(gostKursklausur);

		return temp;
	}

	/**
	 * Berechnet eine Klausurblockung unter Verwendung verschiedener internen Algorithmen.
	 *
	 * @param pInput  Die Menge der Klausuren (Eingabe).
	 * @param pConfig Die Konfiguration der Blockung.
	 * @param pOut    Die Termin-Klausur-Zuordnung (Ausgabe).
	 */
	private void berechne_helper(final @NotNull List<@NotNull GostKursklausur> pInput, final @NotNull KlausurterminblockungAlgorithmusConfig pConfig, final @NotNull List<@NotNull List<@NotNull Long>> pOut) {
		// Logger +4
		_logger.log("KlausurterminblockungAlgorithmus");
		_logger.modifyIndent(+4);

		// End-Zeitpunkt berechnet.
		final long zeitEndeGesamt = System.currentTimeMillis() + pConfig.get_max_time_millis();

		// Random-Objekt erzeugen
		final long seed = _random.nextLong(); // Trick, um den Seed zu kennen.
		final @NotNull Random random = new Random(seed);

		// Erstellung eines Objektes, welches sich um alle dynamischen Daten während des Blockungsvorgangs kümmert.
		final KlausurterminblockungDynDaten dynDaten = new KlausurterminblockungDynDaten(_logger, random, pInput, pConfig);

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


		// Generiere das erste Ergebnis und speichere es in Zustand2 (bisheriges globales Optimum).
		dynDaten.aktion_Clear_TermineNacheinander_GruppeZufaellig();
		dynDaten.aktionZustand2Speichern();

		long zeitProAlgorithmus = 10L; // Weniger ist nicht gut.
		do {
			_logger.log("zeitProAlgorithmus --> " + zeitProAlgorithmus);

			// Alle Algorithmus starten (pro Algorithmus ggf. auch mehrfach).
			for (int iAlgo = 0; iAlgo < algorithmen.length; iAlgo++) {
				final long zeitEndeRunde = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmen[iAlgo].berechne(zeitEndeRunde);
				_logger.log(algorithmen[iAlgo].toString() + " --> " + dynDaten.gibTerminAnzahl());
			}

			zeitProAlgorithmus *= 2; // Nächste Runde hat mehr Zeit.
		} while (System.currentTimeMillis() + algorithmen.length * zeitProAlgorithmus <= zeitEndeGesamt); // noch Zeit?

		// Lade besten globalen Zustand
		dynDaten.aktionZustand2Laden();

		// Fülle die Liste mit Termin-Listen.
		pOut.addAll(dynDaten.gibErzeugeOutput());

		// Logger -4
		_logger.modifyIndent(-4);
	}

}
