package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Schülerverteilungs-Algorithmus verteilt die SuS mit einem bipartiten Matching Algorithmus auf die Kurse. Die
 * Kursdifferenzen werden nicht primär berücksichtigt.
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusSMatchingW extends KursblockungAlgorithmusS {

	/**
	 * Die Anzahl an Runden ohne Verbesserung, bevor es zum Abbruch kommt.
	 */
	private static final int MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG = 5;

	/**
	 * Array der SuS, deren Kurse verteilt werden sollen.
	 */
	private final @NotNull KursblockungDynSchueler @NotNull [] schuelerArr;

	/**
	 * Zur Speicherung einer zufälligen Permutation der Indizes der Schüler.
	 */
	private final @NotNull int[] perm;

	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 *
	 * @param random     Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger     Logger zum Protokollieren von Warnungen und Fehlern.
	 * @param dynDateb   Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusSMatchingW(final @NotNull Random random, final @NotNull Logger logger,
			final @NotNull KursblockungDynDaten dynDateb) {
		super(random, logger, dynDateb);
		schuelerArr = dynDateb.gibSchuelerArray(false);
		perm = KursblockungStatic.gibPermutation(rnd, schuelerArr.length);
	}

	/**
	 * Der Algorithmus verteilt die SuS auf ihre Kurse zufällig. Kommt es während des Verteilens zur Kollision, so wird
	 * der Kurs nicht gewählt.
	 */
	@Override
	public void berechne() {
		// Entfernt die SuS aus allen Kursen.
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();

		// Speicherung der Statistik (Zustand S).
		dynDaten.gibStatistik().aktionBewertungSpeichernS();

		// Optimiere die SuS. Brich ab, wenn die Zeit vorbei ist, oder mehrfach keine Verbesserung erfolgt.
		int countKeineVerbesserung = 0;
		do {
			countKeineVerbesserung = verteileAlleSchueler() ? 0 : (countKeineVerbesserung + 1);
		} while (countKeineVerbesserung < MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG);

	}

	/**
	 * Der Algorithmus verteilt die SuS in zufälliger Reihenfolge ein weiteres Mal zufällig. Falls die Verteilung
	 * schlechter ist, wird der vorherige Zustand wiederhergestellt.
	 *
	 * @return TRUE, falls der Zustand sich verbessert hat.
	 */
	private boolean verteileAlleSchueler() {
		boolean verbesserung = false;

		KursblockungStatic.aktionPermutiere(rnd, perm);
		for (int p = 0; p < schuelerArr.length; p++) {
			final int i = perm[p];
			verbesserung |= verteileEinenSchueler(schuelerArr[i]);
		}

		return verbesserung;
	}

	private boolean verteileEinenSchueler(final @NotNull KursblockungDynSchueler schueler) {
		// Kurszuordnung des Schülers speichern.
		dynDaten.gibStatistik().aktionBewertungSpeichernS();
		schueler.aktionZustandSpeichernS();

		// S. auf seine Kurse neu verteilen.
		schueler.aktionKurseAlleEntfernen();
		schueler.aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs();
		schueler.aktionKurseVerteilenNurMultikurseZufaellig();
		schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtet();

		// Schlechter? --> Kurszuordnung zurück.
		final int cmp = dynDaten.gibStatistik().gibBewertungZustandS1NW2KD();
		if (cmp < 0) {
			schueler.aktionZustandLadenS();
		}

		// Besser?
		return cmp > 0;
	}

}
