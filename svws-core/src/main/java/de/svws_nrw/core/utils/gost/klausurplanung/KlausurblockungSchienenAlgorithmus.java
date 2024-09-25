package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.List;
import java.util.Random;

import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Algorithmus zur Blockung von Klausuren auf eine minimale Anzahl von Schienen
 * (ergo Klausurtage).
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurblockungSchienenAlgorithmus {

	private static final @NotNull Random _random = new Random();

	/** Ein Logger für Debug-Zwecke. */
	private final @NotNull Logger _logger;

	/**
	 * Der Konstruktor.
	 */
	public KlausurblockungSchienenAlgorithmus() {
		_logger = new Logger();
	}

	/**
	 * Der Konstruktor.
	 *
	 * @param pLogger  Ein Logger für Debug-Zwecke.
	 */
	public KlausurblockungSchienenAlgorithmus(final @NotNull Logger pLogger) {
		_logger = pLogger;
	}

	/**
	 * Berechnet die Blockung von Klausuren
	 *
	 * @param pInput          Die Eingabe beinhaltet alle Klausuren, welche die SuS beinhalten.
	 * @param pMaxTimeMillis  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @return Eine Liste von Listen: 1. Ebene = Schienen, 2. Ebene = KlausurIDs
	 */
	public @NotNull List<List<Long>> berechne(final @NotNull List<GostKursklausurRich> pInput, final long pMaxTimeMillis) {

		// End-Zeitpunkt berechnet.
		final long zeitEndeGesamt = System.currentTimeMillis() + pMaxTimeMillis;

		// Random-Objekt erzeugen
		final long seed = _random.nextLong(); // Trick, um den Seed zu kennen.
		final @NotNull Random random = new Random(seed);

		// Konvertierung: KlausurblockungSchienenInput --> KlausurblockungSchienenDynDaten
		final KlausurblockungSchienenDynDaten dynDaten = new KlausurblockungSchienenDynDaten(_logger, random, pInput);

		// Algorithmen erzeugen
		final @NotNull KlausurblockungSchienenAlgorithmusAbstract @NotNull [] algorithmen = new KlausurblockungSchienenAlgorithmusAbstract @NotNull [] {
				// Alle Algorithmen zur Verteilung von Klausuren auf ihre Schienen ...
				new KlausurblockungSchienenAlgorithmusGreedy3(random, dynDaten), // Backtracking
				new KlausurblockungSchienenAlgorithmusGreedy4(random, dynDaten), // DSatur
				new KlausurblockungSchienenAlgorithmusGreedy1(random, dynDaten), // Klausuren zufällig & Schienen zufällig
				new KlausurblockungSchienenAlgorithmusGreedy1b(random, dynDaten), // Klausuren zufällig & Schienen nacheinander
				new KlausurblockungSchienenAlgorithmusGreedy2(random, dynDaten), // Klausuren nach Knotengrad & Schienen zufällig
				new KlausurblockungSchienenAlgorithmusGreedy2b(random, dynDaten), // Klausuren nach Knotengrad & Schienen nacheinander
				new KlausurblockungSchienenAlgorithmusGreedy5(random, dynDaten), // Simulated Annealing
				new KlausurblockungSchienenAlgorithmusGreedy6(random, dynDaten), // Recursive Largest First (RLF)
				new KlausurblockungSchienenAlgorithmusGreedy7(random, dynDaten), // Termine aufsteigend, Klausuren zufällig
				// ... Ende der Algorithmen.
		};


		// Blockungsschleife
		dynDaten.aktion_EntferneAlles_SchienenNacheinander_KlausurenZufaellig();
		dynDaten.aktionZustand2Speichern();

		long zeitProAlgorithmus = 10L; // Weniger ist nicht gut.
		do {
			// Alle Algorithmus starten (pro Algorithmus ggf. auch mehrfach).
			for (final @NotNull KlausurblockungSchienenAlgorithmusAbstract algorithmus : algorithmen) {
				final long zeitEndeRunde = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmus.berechne(zeitEndeRunde);
			}

			zeitProAlgorithmus *= 2; // Nächste Runde hat mehr Zeit.
		} while ((System.currentTimeMillis() + (algorithmen.length * zeitProAlgorithmus)) <= zeitEndeGesamt); // noch Zeit?

		// Lade besten globalen Zustand
		dynDaten.aktionZustand2Laden();

		return dynDaten.gibErzeugeOutput();
	}

}
