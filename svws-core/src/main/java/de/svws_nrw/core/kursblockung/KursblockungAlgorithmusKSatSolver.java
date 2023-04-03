package de.svws_nrw.core.kursblockung;

import java.util.HashMap;
import java.util.Random;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.kursblockung.satsolver.SatSolver3;
import de.svws_nrw.core.kursblockung.satsolver.SatSolverA;
import de.svws_nrw.core.kursblockung.satsolver.SatSolverWrapper;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus des <b>Typs K</b> verteilt die Kurse auf ihre Schienen. Die Strategie <b>SatSolver</b> erzeugt
 * eine Formel mit Boolschen-Variablen und übergibt diese einem SAT-Solver zur Berechnung. Findet der SAT-Solver eine
 * Lösung, dann wird versucht eine neue Lösung mit weniger Nichtwählern zu finden.
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusKSatSolver extends KursblockungAlgorithmusK {

	private final @NotNull KursblockungDynKurs @NotNull [] kurseAlle;
	private final @NotNull KursblockungDynSchueler @NotNull [] schuelerAlle;
	private int maxNichtWaehler;

	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param dynDat  Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusKSatSolver(final @NotNull Random pRandom, final @NotNull Logger logger,
			final @NotNull KursblockungDynDaten dynDat) {
		super(pRandom, logger, dynDat);
		kurseAlle = dynDat.gibKurseAlle();
		schuelerAlle = dynDaten.gibSchuelerArray(false);
		maxNichtWaehler = 10;
	}

	@Override
	public void berechne(final long pMaxTimeMillis) {
		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0) {
			return;
		}

		// Startzeit speichern.
		final long timeStart = System.currentTimeMillis();

		// Entferne SuS aus den Kursen (vorsichtshalber wegen alter Berechnungen).
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();

		// Verteile die Kurse beim ersten Start zufällig.
		dynDaten.aktionKurseFreieZufaelligVerteilen();

		// Speicherung des Start-Zustandes.
		dynDaten.aktionZustandSpeichernK();

		// Optimimiere, solange noch Zeit ist ...
		int difNichtWaehler = 1;
		while (System.currentTimeMillis() - timeStart < pMaxTimeMillis) {
			// Berechne eine Lösung mit dem SAT-Solver.
			final int result = berechneSchritt(pMaxTimeMillis);

			// Zeit reichte nicht?
			if (result == SatSolverA.RESULT_UNKNOWN) {
				return;
			}

			// Ergebnis gefunden? --> geht auch weniger Nichtwähler?
			if (result == SatSolverA.RESULT_SATISFIABLE) {
				maxNichtWaehler = Math.max(maxNichtWaehler - difNichtWaehler, 0);
				continue;
			}

			// Ergebnis ist UNSAT --> Nichtwähler muss um 1 erhöht werden.
			maxNichtWaehler++;
			difNichtWaehler = 0;

		}

	}

	/**
	 * Erzeugt eine Formel in konjunktiver Normalform (CNF) und übergibt sie dem SAT-Solver. Der Solver versucht die
	 * Formel innerhalb des Zeitlimits zu lösen. Falls er es nicht schafft, dann wurde zuvor eine zufällige
	 * Kursverteilung definiert.
	 *
	 * @param pMaxTimeMillis Die maximale Blockungszeit in Millisekunde.
	 *
	 * @return Liefert eines der drei möglichen Ergebnisse {@link SatSolverA#RESULT_SATISFIABLE oder
	 *         SatSolverI#RESULT_UNKNOWN oder SatSolverI#RESULT_UNSATISFIABLE. }
	 */
	public int berechneSchritt(final long pMaxTimeMillis) {

		final @NotNull
		SatSolverWrapper ssw = new SatSolverWrapper(new SatSolver3(_random));
		final int nSchienen = dynDaten.gibSchienenAnzahl();

		// SAT-SOLVER: Variablen von "kursInSchiene"
		final @NotNull
		HashMap<@NotNull KursblockungDynKurs, @NotNull int[]> mapKursSchiene = new HashMap<>();
		for (final @NotNull
				KursblockungDynKurs kurs : kurseAlle) {
			final @NotNull
			int[] schienen = new int[nSchienen];
			for (int s = 0; s < nSchienen; s++) {
				schienen[s] = kurs.gibIstSchieneFixiert(s) ? ssw.getVarTRUE()
						: kurs.gibIstSchieneGesperrt(s) ? ssw.getVarFALSE() : ssw.createNewVar();
			}
			mapKursSchiene.put(kurs, schienen);
		}

		// SAT-SOLVER: Variablen von "schuelerInKurs"
		final @NotNull
		HashMap<@NotNull KursblockungDynSchueler, @NotNull HashMap<@NotNull KursblockungDynFachart, @NotNull Integer>> mapSchuelerFachartNichtwahl = new HashMap<>();
		final @NotNull
		HashMap<@NotNull KursblockungDynSchueler, @NotNull HashMap<@NotNull KursblockungDynKurs, @NotNull Integer>> mapSchuelerIstInKurs = new HashMap<>();

		final @NotNull
		LinkedCollection<@NotNull Integer> listNichtwahlen = new LinkedCollection<>();
		for (final @NotNull
				KursblockungDynSchueler schueler : schuelerAlle) {
			mapSchuelerFachartNichtwahl.put(schueler, new HashMap<>());
			mapSchuelerIstInKurs.put(schueler, new HashMap<>());
			for (final @NotNull
					KursblockungDynFachart fachart : schueler.gibFacharten()) {
				final int varNichtwahlen = ssw.createNewVar();
				final HashMap<@NotNull KursblockungDynFachart, @NotNull Integer> mapFachartNichtwahl = mapSchuelerFachartNichtwahl
						.get(schueler);
				if (mapFachartNichtwahl == null)
					throw new NullPointerException();
				mapFachartNichtwahl.put(fachart, varNichtwahlen);
				listNichtwahlen.add(varNichtwahlen);
				for (final @NotNull
						KursblockungDynKurs kurs : fachart.gibKurse()) {
					final int varKurs = ssw.createNewVar();
					final HashMap<@NotNull KursblockungDynKurs, @NotNull Integer> mapIstInKurs = mapSchuelerIstInKurs
							.get(schueler);
					if (mapIstInKurs == null)
						throw new NullPointerException();
					mapIstInKurs.put(kurs, varKurs);
				}
			}
		}

		// Forciere: Jeder Kurs in GENAU ... Schienen
		for (final @NotNull
				KursblockungDynKurs kurs : kurseAlle) {
			final @NotNull
			LinkedCollection<@NotNull Integer> list = new LinkedCollection<>();
			final int[] schienen = mapKursSchiene.get(kurs);
			if (schienen == null)
				throw new NullPointerException();
			for (int s = 0; s < nSchienen; s++) {
				list.add(schienen[s]);
			}
			final int amount = kurs.gibSchienenAnzahl();
			ssw.c_exactly_GENERIC(list, amount);
		}

		// Forciere: Jeder Schüler hat pro Fach genau ein Kurs
		for (final @NotNull
				KursblockungDynSchueler schueler : schuelerAlle) {
			for (final @NotNull
					KursblockungDynFachart fachart : schueler.gibFacharten()) {
				final @NotNull
				LinkedCollection<@NotNull Integer> list = new LinkedCollection<>();
				for (final KursblockungDynKurs kurs : fachart.gibKurse()) {
					final HashMap<@NotNull KursblockungDynKurs, @NotNull Integer> mapIstInKurs = mapSchuelerIstInKurs
							.get(schueler);
					if (mapIstInKurs == null)
						throw new NullPointerException();
					final Integer varKurs = mapIstInKurs.get(kurs);
					if (varKurs == null)
						throw new NullPointerException();
					list.add(varKurs);
				}
				ssw.c_exactly_GENERIC(list, 1);
			}
		}

		// Forciere: Jeder Schüler: Wenn zwei Kurse gewählt, dann nicht gleiche Schiene.
		// int sCount = 0;
		for (final @NotNull
				KursblockungDynSchueler schueler : schuelerAlle) {
			final HashMap<@NotNull KursblockungDynKurs, @NotNull Integer> mapIstInKurs = mapSchuelerIstInKurs.get(schueler);
			if (mapIstInKurs == null)
				throw new NullPointerException();
			// sCount++;
			for (final @NotNull
					KursblockungDynFachart fachart1 : schueler.gibFacharten()) {
				for (final @NotNull
						KursblockungDynFachart fachart2 : schueler.gibFacharten()) {
					if (fachart1.gibNr() < fachart2.gibNr()) {
						for (final @NotNull
								KursblockungDynKurs kurs1 : fachart1.gibKurse()) {
							for (final @NotNull
									KursblockungDynKurs kurs2 : fachart2.gibKurse()) {
								final Integer var1 = mapIstInKurs.get(kurs1);
								final Integer var2 = mapIstInKurs.get(kurs2);
								if ((var1 == null) || (var2 == null))
									throw new NullPointerException();
								final int x = ssw.c_new_var_AND(var1, var2);

								for (int s = 0; s < nSchienen; s++) {
									final int[] schienenKurs1 = mapKursSchiene.get(kurs1);
									final int[] schienenKurs2 = mapKursSchiene.get(kurs2);
									if ((schienenKurs1 == null) || (schienenKurs2 == null))
										throw new NullPointerException();
									final int var3 = schienenKurs1[s];
									final int var4 = schienenKurs2[s];
									// NOT (var1 AND var2 AND var3 AND var4)
									ssw.c_3(-x, -var3, -var4);
								}
							}
						}
					}
				}
			}

		}

		// Forciere: Nichtwahlen <= ...
		// ssa.c_at_most_GENERIC(listNichtwahlen, maxNichtWaehler);

		// BERECHNUNG
		System.out.println("V=" + ssw.getVarCount() + ", C=" + ssw.getClauseCount());
		final int satresult = ssw.solve(pMaxTimeMillis);

		// Abbrechen, wenn nicht gelöst.
		if (satresult != SatSolverA.RESULT_SATISFIABLE) {
			return satresult;
		}

		// Entferne SuS aus den Kursen (vorsichtshalber wegen alter Berechnungen).
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();

		// Verteile die Kurse so, wie der SAT-Solver es vorschlägt.
		for (final @NotNull
				KursblockungDynKurs kurs : kurseAlle) {
			final @NotNull
			LinkedCollection<@NotNull Integer> schienen = new LinkedCollection<>();
			for (int s = 0; s < nSchienen; s++) {
				final int[] schienenKurs = mapKursSchiene.get(kurs);
				if (schienenKurs == null)
					throw new NullPointerException();
				if (ssw.isVarTrue(schienenKurs[s])) {
					schienen.add(s);
				}
			}
			kurs.aktionVerteileAufSchienen(schienen);
		}

		return satresult;
	}

}
