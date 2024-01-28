package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Schülerverteilungs-Algorithmus verteilt die SuS zufällig auf ihre Kurse in mehreren Runden. Sobald die
 * Verbesserung stagniert, bricht der Algorithmus ab. Da die Datenstruktur {@link KursblockungDynDaten} pro Fachart die
 * Kurse nach ihrer Kursgröße aufsteigend sortiert, findet der Algorithmus sogar sehr gute Kursdifferenzen.
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusSZufaellig extends KursblockungAlgorithmusS {

	/**
	 * Die Anzahl an Runden ohne Verbesserung, bevor es zum Abbruch kommt.
	 */
	private static final int MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG = 30;

	/**
	 * Array der SuS, deren Kurse verteilt werden sollen.
	 */
	private final @NotNull KursblockungDynSchueler @NotNull [] _schuelerArr;

	/**
	 * Zur Speicherung einer zufälligen Permutation der Indizes der Schüler.
	 */
	private final @NotNull int[] _perm;

	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in dieser Methode noch nicht
	 * auf Schienen verteilt werden.
	 *
	 * @param random Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger Logger zum Protokollieren von Warnungen und Fehlern.
	 * @param dynDat Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusSZufaellig(final @NotNull Random random, final @NotNull Logger logger, final @NotNull KursblockungDynDaten dynDat) {
		super(random, logger, dynDat);
		_schuelerArr = dynDat.gibSchuelerArrayAlle();
		_perm = KursblockungStatic.gibPermutation(_random, _schuelerArr.length);
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
			countKeineVerbesserung = verteileSchuelerAlle() ? 0 : countKeineVerbesserung + 1;
		} while (countKeineVerbesserung < MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG);

	}

	/**
	 * Der Algorithmus verteilt die SuS in zufälliger Reihenfolge ein weiteres Mal zufällig. Falls die Verteilung
	 * schlechter ist, wird der vorherige Zustand wiederhergestellt.
	 *
	 * @return TRUE, falls der Zustand sich verbessert hat.
	 */
	private boolean verteileSchuelerAlle() {
		boolean verbesserung = false;

		KursblockungStatic.aktionPermutiere(_random, _perm);
		for (int p = 0; p < _schuelerArr.length; p++) {
			final int i = _perm[p];
			verbesserung |= verteileSchuelerEiner(_schuelerArr[i]);
		}

		return verbesserung;
	}

	private boolean verteileSchuelerEiner(final @NotNull KursblockungDynSchueler schueler) {
		// Kurszuordnung des Schülers speichern.
		dynDaten.gibStatistik().aktionBewertungSpeichernS();
		schueler.aktionZustandSpeichernS();

		// Schueler auf seine Kurse neu verteilen.
		schueler.aktionKurseAlleEntfernen();
		schueler.aktionKurseVerteilenZufaellig();

		// Schlechter? --> Kurszuordnung zurück.
		final int cmp = dynDaten.gibStatistik().gibBewertungZustandS_NW_KD();
		if (cmp < 0)
			schueler.aktionZustandLadenS();

		// Besser?
		return cmp > 0;
	}

}
