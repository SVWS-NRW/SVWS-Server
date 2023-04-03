package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Kursverteilungs-Algorithmus verteilt die Kurse auf ihre Schienen, indem es die Kurslage zufällig verändert und
 * dabei versucht die Nichtwahlen zu minimieren. Die minimale Anzahl an Nichtwahlen wird mit einem
 * Bipartiten-Matching-Algorithmus berechnet. Bei dieser Variante wird das Matching nur berechnet, SuS werden aber nicht
 * den Kursen hinzugefügt.
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusKFachwahlmatrix2 extends KursblockungAlgorithmusK {

	/**
	 * Die Anzahl an Runden ohne Verbesserung, bevor es zum Abbruch kommt.
	 */
	private static final int MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG = 2000;

	private final @NotNull KursblockungDynSchueler @NotNull [] schuelerAlle;

	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDat Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusKFachwahlmatrix2(final @NotNull Random pRandom, final @NotNull Logger pLogger,
			final @NotNull KursblockungDynDaten pDynDat) {
		super(pRandom, pLogger, pDynDat);
		schuelerAlle = dynDaten.gibSchuelerArray(false);
	}

	/**
	 * Der Algorithmus entfernt zunächst alle SuS aus ihren Kursen. Anschließend werden die Kurse zufällig verteilt.
	 * Anschließend verändert der Algorithmus die Lage eines zufälligen Kurses. Falls sich die Bewertung verschlechtert,
	 * wird die Veränderung rückgängig gemacht.
	 */
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

		// Optimiere die Kurse. Bricht ab, wenn die Zeit vorbei ist, oder mehrfach keine Verbesserung erfolgt.
		int countKeineVerbesserung = 0;
		do {
			countKeineVerbesserung = verteileKurse() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG)
				&& (System.currentTimeMillis() - timeStart < pMaxTimeMillis));

	}

	/**
	 * Die Lage einiger Kurse wird verändert. Falls sich die Bewertung verschlechtert, wird die Veränderung rückgängig
	 * gemacht.
	 *
	 * @return true, wenn der Zustand angepasst wurde
	 */
	private boolean verteileKurse() {
		// Ein 1-* Kurse wandern zufällig in eine andere Schiene.
		do {
			// Entferne SuS, sonst dürfen Kurse nicht die Schiene wechseln.
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();

			// Einen Kurs zufällig verteilen.
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien();

			// SuS verteilen
			verteileSuS();

			// Besser? --> Speichern.
			if (dynDaten.gibBewertungK_FW_NW_KD_JetztBesser() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return true;
			}
		} while (_random.nextBoolean());

		// Schlechter
		dynDaten.aktionZustandLadenK();
		return false;
	}

	/**
	 * Verteilt die SuS. Multikurse der SuS werden zufällig verteilt, die anderen Kurse werden mit Hilfe eines
	 * Matching-Algorithmus verteilt.
	 */
	private void verteileSuS() {
		final @NotNull
		int[] perm = KursblockungStatic.gibPermutation(_random, schuelerAlle.length);

		for (int p = 0; p < perm.length; p++) {
			final int i = perm[p];
			final KursblockungDynSchueler schueler = schuelerAlle[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenMitBipartiteMatching();
		}

	}

}
