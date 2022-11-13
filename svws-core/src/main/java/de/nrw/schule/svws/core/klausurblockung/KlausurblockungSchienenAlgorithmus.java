package de.nrw.schule.svws.core.klausurblockung;

import java.util.Random;

import de.nrw.schule.svws.core.Service;
import de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenInput;
import de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenOutputs;
import de.nrw.schule.svws.core.logger.LogLevel;
import jakarta.validation.constraints.NotNull;

/** Algorithmus zur Blockung von Klausuren auf eine minimale Anzahl von Schienen (ergo Klausurtage).
 * 
 * @author Benjamin A. Bartsch */
public class KlausurblockungSchienenAlgorithmus
		extends Service<KlausurblockungSchienenInput, KlausurblockungSchienenOutputs> {

	@Override
	public @NotNull KlausurblockungSchienenOutputs handle(@NotNull KlausurblockungSchienenInput pInput) {
		// End-Zeitpunkt berechnet.
		long zeitEndeGesamt = System.currentTimeMillis() + pInput.maxTimeMillis;

		// Logger-Einrückung (relativ +4).
		logger.modifyIndent(+4);

		// Random-Objekt erzeugen
		long seed = new Random().nextLong(); // Trick, um den Seed zu kennen.
		@NotNull Random random = new Random(seed);
		logger.log(LogLevel.APP, "KlausurblockungSchienenAlgorithmus: pInput.datenbankID=" + pInput.datenbankID
				+ ", pInput.maxTimeMillis=" + pInput.maxTimeMillis + ", seed=" + seed);

		// Konvertierung: KlausurblockungSchienenInput --> KlausurblockungSchienenDynDaten
		KlausurblockungSchienenDynDaten dynDaten = new KlausurblockungSchienenDynDaten(random, logger, pInput);

		// Algorithmen erzeugen
		@NotNull KlausurblockungSchienenAlgorithmusAbstract @NotNull [] algorithmen = new KlausurblockungSchienenAlgorithmusAbstract @NotNull [] {
				// Alle Algorithmen zur Verteilung von Klausuren auf ihre Schienen ...
				new KlausurblockungSchienenAlgorithmusGreedy3(random, logger, dynDaten), // Backtracking
				new KlausurblockungSchienenAlgorithmusGreedy4(random, logger, dynDaten), // DSatur
				new KlausurblockungSchienenAlgorithmusGreedy1(random, logger, dynDaten), // Klausuren zufällig & Schienen zufällig
				new KlausurblockungSchienenAlgorithmusGreedy1b(random, logger, dynDaten), // Klausuren zufällig & Schienen nacheinander
				new KlausurblockungSchienenAlgorithmusGreedy2(random, logger, dynDaten), // Klausuren nach Knotengrad & Schienen zufällig
				new KlausurblockungSchienenAlgorithmusGreedy2b(random, logger, dynDaten), // Klausuren nach Knotengrad & Schienen nacheinander
				new KlausurblockungSchienenAlgorithmusGreedy5(random, logger, dynDaten), // Simulated Annealing
				new KlausurblockungSchienenAlgorithmusGreedy6(random, logger, dynDaten), // Recursive Largest First (RLF)
				// ... Ende der Algorithmen.
		};

		// Sammeln der Ausgaben
		KlausurblockungSchienenOutputs result = new KlausurblockungSchienenOutputs();

		// Blockungsschleife
		// System.out.println("----------------------------------------------------");
		long zeitProAlgorithmus = 10L; // Weniger ist nicht gut.
		do {
			// System.out.println("zeitProAlgorithmus --> " + zeitProAlgorithmus);

			// Alle Algorithmus starten (pro Algorithmus ggf. auch mehrfach).
			for (int iAlgo = 0; iAlgo < algorithmen.length; iAlgo++) {
				long zeitEndeRunde = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmen[iAlgo].berechne(zeitEndeRunde);
				result.ergebnisse.add(dynDaten.gibErzeugeOutput());

				// System.out.println(" " + dynDaten.gibAnzahlSchienen() + " Schienen --> " + algorithmen[iAlgo].toString());
			}

			zeitProAlgorithmus *= 2; // Nächste Runde hat mehr Zeit.
		} while (System.currentTimeMillis() + algorithmen.length * zeitProAlgorithmus <= zeitEndeGesamt); // noch Zeit?

		// Logger-Einrückung (relativ -4)
		logger.modifyIndent(-4);

		return result;
	}

}
