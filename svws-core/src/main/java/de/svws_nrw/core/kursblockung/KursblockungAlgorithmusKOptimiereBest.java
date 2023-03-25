package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus des <b>Typs K</b> verteilt die Kurse auf ihre Schienen. Die Strategie <b>KOptimiereBest</b> lädt
 * das derzeit beste Blockungsergebnis und versucht dieses systematisch weiter zu optimieren.
 * 
 * @author Benjamin A. Bartsch
 */
public class KursblockungAlgorithmusKOptimiereBest extends KursblockungAlgorithmusK {

	/**
	 * Mit diesem Algorithmus werden die SuS verteilt.
	 */
	private final @NotNull KursblockungAlgorithmusSSchnellW algoS;

	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDat Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusKOptimiereBest(final @NotNull Random pRandom, final @NotNull Logger pLogger,
			final @NotNull KursblockungDynDaten pDynDat) {
		super(pRandom, pLogger, pDynDat);
		algoS = new KursblockungAlgorithmusSSchnellW(pRandom, pLogger, pDynDat);
	}

	@Override
	public @NotNull String toString() {
		return "OptimiereBest";
	}

	@Override
	public void berechne(final long pEndzeit) {
		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0)
			return;

		// Lade beste globale Blockung.
		dynDaten.aktionZustandLadenG();

		// Speicherung des Start-Zustandes.
		dynDaten.aktionZustandSpeichernK();

		// Optimiere die Kurse. Bricht ab, wenn die Zeit vorbei ist.
		do {
			veraendereDieKurslageZufaelligEinWenig();
		} while (System.currentTimeMillis() < pEndzeit);

	}

	/**
	 * Kurslage wird ein wenig zufällig verändert und bewertet. Falls sich die Bewertung verschlechtert, wird die
	 * Veränderung rückgängig gemacht.
	 */
	private void veraendereDieKurslageZufaelligEinWenig() {
		// Ein 1-* Kurse wandern zufällig in eine andere Schiene.
		do {
			// Entferne SuS, sonst dürfen Kurse nicht die Schiene wechseln.
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();

			// Einen Kurs zufällig verteilen.
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien();

			// SuS verteilen
			algoS.berechne();

			// Besser? --> Speichern und Abbruch
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return;
			}
		} while (_random.nextBoolean());

		// Verschlechterung rückgängig machen.
		dynDaten.aktionZustandLadenK();
	}

}
