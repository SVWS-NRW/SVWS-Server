package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;

import de.nrw.schule.svws.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Kursverteilungs-Algorithmus verteilt die Kurse auf ihre Schienen, indem es die Kurslage zufällig verändert und
 * dabei versucht die Nichtwahlen zu minimieren. Die minimale Anzahl an Nichtwahlen wird mit einem
 * Bipartiten-Matching-Algorithmus berechnet.
 * 
 * @author Benjamin A. Bartsch
 */
public class KursblockungAlgorithmusKMatching extends KursblockungAlgorithmusK {

	/**
	 * Die Anzahl an Runden ohne Verbesserung, bevor es zum Abbruch kommt.
	 */
	private static final int MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG = 2000;

	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDat Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusKMatching(@NotNull Random pRandom, @NotNull Logger pLogger,
			@NotNull KursblockungDynDaten pDynDat) {
		super(pRandom, pLogger, pDynDat);
	}
	
	@Override
	public @NotNull String toString() {
		return "Matching";
	}

	/**
	 * Der Algorithmus verändert die Kurslage zufällig und bewertet die neue Lage mit dem Algorithmus
	 * {@link KursblockungAlgorithmusSMatching}. Falls sich die Bewertung verschlechtert, wird die Veränderung
	 * rückgängig gemacht.
	 */
	@Override
	public void berechne(long pEndzeit) {
		long current = System.currentTimeMillis();
		long halbzeit = current + (pEndzeit - current) / 2;

		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0)
			return;

		// Entferne SuS aus den Kursen (vorsichtshalber wegen alter Berechnungen).
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();

		// Verteile die Kurse beim ersten Start zufällig.
		dynDaten.aktionKurseFreieZufaelligVerteilen();

		// Speicherung des Start-Zustandes.
		dynDaten.aktionZustandSpeichernK();

		// Optimiere die Kurse. Bricht ab, wenn die Zeit vorbei ist, oder mehrfach keine Verbesserung erfolgt.
		int countKeineVerbesserung = 0;
		do {
			countKeineVerbesserung = verteileKurseMitMatching() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG)
				&& (System.currentTimeMillis() < halbzeit));

		countKeineVerbesserung = 0;
		do {
			countKeineVerbesserung = verteileKurseMitMatchingW() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG)
				&& (System.currentTimeMillis() < pEndzeit));

	}

	/**
	 * Die Lage einiger Kurse wird verändert. Falls sich die Bewertung verschlechtert, wird die Veränderung rückgängig
	 * gemacht.
	 */
	private boolean verteileKurseMitMatching() {
		// Ein 1-* Kurse wandern zufällig in eine andere Schiene.
		do {
			// Entferne SuS, sonst dürfen Kurse nicht die Schiene wechseln.
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();

			// Einen Kurs zufällig verteilen.
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien();

			// SuS verteilen
			dynDaten.aktionSchuelerVerteilenMitBipartitemMatching();

			// Besser? --> Speichern.
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return true;
			}
		} while (_random.nextBoolean());

		// Verschlechterung rückgängig machen.
		dynDaten.aktionZustandLadenK();
		return false;
	}

	/**
	 * Kurslage wird ein wenig zufällig verändert und bewertet. Falls sich die Bewertung verschlechtert, wird die
	 * Veränderung rückgängig gemacht.
	 */
	private boolean verteileKurseMitMatchingW() {
		// Ein 1-* Kurse wandern zufällig in eine andere Schiene.
		do {
			// Entferne SuS, sonst dürfen Kurse nicht die Schiene wechseln.
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();

			// Einen Kurs zufällig verteilen.
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien();

			// SuS verteilen
			dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();

			// Besser? --> Speichern.
			int cmp = dynDaten.gibCompareZustandK_NW_KD_FW();
			if (cmp > 0) {
				dynDaten.aktionZustandSpeichernK();
				return true;
			}
		} while (_random.nextBoolean());

		// Schlechter
		dynDaten.aktionZustandLadenK();
		return false;
	}

}
