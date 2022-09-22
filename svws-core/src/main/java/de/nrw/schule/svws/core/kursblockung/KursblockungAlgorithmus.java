package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;
import java.util.Vector;

import de.nrw.schule.svws.core.Service;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputs;
import de.nrw.schule.svws.logger.LogLevel;
import jakarta.validation.constraints.NotNull;

/** Dieser Service wandelt die Eingabedaten {@link KursblockungInput} in dynamische Blockungsdaten
 * {@link KursblockungDynDaten} um, startet dann den Kursblockungsalgorithmus, welcher die Blockungsdaten manipuliert
 * und wandelt zuletzt {@link KursblockungDynDaten} in die Ausgabedaten {@link KursblockungOutputs} um. Der Service
 * überschreitet dabei nicht die Zeit, die in {@link KursblockungInput#maxTimeMillis} festgelegt wurde.
 * 
 * @author Benjamin A. Bartsch */
public class KursblockungAlgorithmus extends Service<@NotNull KursblockungInput, @NotNull KursblockungOutputs> {

	@Override
	public @NotNull KursblockungOutputs handle(@NotNull KursblockungInput pInput) {
		// Logger-Einrückung (relativ +4).
		logger.modifyIndent(+4);

		// Random-Objekt erzeugen
		// Größter Integer Wert in TypeScript --> 9007199254740991L
		long seed = (pInput.seed == 0) ? (new Random()).nextLong() : pInput.seed;
		@NotNull Random random = new Random(seed);
		logger.log(LogLevel.APP, "Seed = " + pInput.seed + " wird nicht verwendet --> Transpiler kennt das nicht.");
		logger.log(LogLevel.APP, "Erster nextInt() Aufruf liefert " + seed);

		// Konvertierung von 'KursblockungInput' zu 'KursblockungDynamischeDaten'.
		@NotNull KursblockungDynDaten dynDaten = new KursblockungDynDaten(random, logger, pInput);
		long zeitBedarf = dynDaten.gibBlockungszeitMillis();
		long zeitEndeGesamt = System.currentTimeMillis() + zeitBedarf;

		// Vorbereitung der Rückgabe an die GUI.
		@NotNull KursblockungOutputs kursblockungOutputs = new KursblockungOutputs();
		kursblockungOutputs.outputs = new Vector<>();

		@NotNull KursblockungAlgorithmusK @NotNull [] algorithmenK = new KursblockungAlgorithmusK @NotNull [] {
				// Alle Algorithmen zur Verteilung von Kursen auf ihre Schienen ...
				new KursblockungAlgorithmusKSchnellW(random, logger, dynDaten),
				new KursblockungAlgorithmusKFachwahlmatrix(random, logger, dynDaten),
				new KursblockungAlgorithmusKMatching(random, logger, dynDaten),
				new KursblockungAlgorithmusKSchuelervorschlag(random, logger, dynDaten),
				new KursblockungAlgorithmusKOptimiereBest(random, logger, dynDaten),
				// ... Ende der K-Algorithmen.
		};

		@NotNull KursblockungAlgorithmusS @NotNull [] algorithmenS = new KursblockungAlgorithmusS @NotNull [] {
				// Alle Algorithmen zur Verteilung von SuS auf ihre Kurse ...
				new KursblockungAlgorithmusSSchnellW(random, logger, dynDaten),
				new KursblockungAlgorithmusSZufaellig(random, logger, dynDaten),
				new KursblockungAlgorithmusSMatching(random, logger, dynDaten),
				new KursblockungAlgorithmusSMatchingW(random, logger, dynDaten),
				// ... Ende der S-Algorithmen.
		};

		// Hauptschleife: Jeder Algorithmus-K erhält stetig mehr Zeit.
		long zeitProK = 100l; // weniger als 100 führt zu unnützen Ergebnissen
		do {
			// Jeder Algorithmus-K wird ausprobiert.
			for (int iK = 0; iK < algorithmenK.length; iK++) {

				// Ggf. läuft ein AlgorithmusK auch erneut, falls er schneller als seine Maximalzeit war.
				long zeitEndeK = System.currentTimeMillis() + zeitProK;
				do {
					// System.out.println("Zeit " + zeitProK + " Algorithmus " + iK);
					verwendeAlgorithmusK(algorithmenK[iK], zeitEndeK, dynDaten, algorithmenS, kursblockungOutputs, seed,
							pInput.input);
				} while (System.currentTimeMillis() < zeitEndeK);

				// Zeit abgelaufen?
				if (System.currentTimeMillis() + zeitProK > zeitEndeGesamt)
					break;

			}
			// Nächster Durchgang hat mehr Zeit.
			zeitProK += 100;
		} while (System.currentTimeMillis() < zeitEndeGesamt);

		// Logger-Einrückung (relativ -4)
		logger.modifyIndent(-4);

		// Rückgabe aller Blockungsergebnisse.
		return kursblockungOutputs;
	}

	private static void verwendeAlgorithmusK(@NotNull KursblockungAlgorithmusK kursblockungAlgorithmusK, long zeitEndeK,
			@NotNull KursblockungDynDaten dynDaten, @NotNull KursblockungAlgorithmusS @NotNull [] algorithmenS,
			@NotNull KursblockungOutputs kursblockungOutputs, long inputSeed, long inputID) {

		// Verteilung der Kurse.
		kursblockungAlgorithmusK.berechne(zeitEndeK);

		// Verteilung der SuS (nur die beste Verteilung bleibt im Zustand K).
		dynDaten.aktionZustandSpeichernK();

		for (int iS = 0; iS < algorithmenS.length; iS++) {
			// Verteilung der SuS
			algorithmenS[iS].berechne();

			// Bessere SuS-Verteilung gefunden?
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0)
				dynDaten.aktionZustandSpeichernK();
		}

		// Bestes Ergebnis laden (Zustand K).
		dynDaten.aktionZustandLadenK();

		// Gibt es einen neuen besten globalen Zustand?
		if (dynDaten.gibCompareZustandG_NW_KD_FW() > 0) {
			// dynDaten.debug();
			// dynDaten.gibStatistik().debug("*" + kursblockungAlgorithmusK);
			dynDaten.aktionZustandSpeichernG();
		}

		// Aktuellen Stand der Blockung speichern.
		@NotNull KursblockungOutput out = dynDaten.gibErzeugtesKursblockungOutput(inputSeed, inputID);
		kursblockungOutputs.outputs.add(out);

	}

}
