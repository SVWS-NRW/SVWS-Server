package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;

import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus des <b>Typs K</b> verteilt die Kurse auf ihre Schienen. Die Strategie <b>Fachwahlmatrix</b>
 * optimiert Kurse auf Basis einer 2D-Matrix, die zu je zwei Fachwahlen angibt, wie oft diese Kombination gewählt wurde.
 * Der Algorithmus versucht nur solche Kurs-Paarungen in eine Schiene zu tun, deren paarweise Wahl gering ist.
 * 
 * @author Benjamin A. Bartsch
 */
public class KursblockungAlgorithmusKFachwahlmatrix extends KursblockungAlgorithmusK {

	/**
	 * Die Anzahl an Runden ohne Verbesserung, bevor es zum Abbruch kommt.
	 */
	private static final int MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG = 5000;

	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDat Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusKFachwahlmatrix(@NotNull Random pRandom, @NotNull Logger pLogger,
			@NotNull KursblockungDynDaten pDynDat) {
		super(pRandom, pLogger, pDynDat);
	}
	
	@Override
	public @NotNull String toString() {
		return "Fachwahlmatrix";
	}

	/**
	 * Der Algorithmus entfernt zunächst alle SuS aus ihren Kursen. Anschließend werden die Kurse zufällig verteilt.
	 */
	@Override
	public void berechne(long pEndzeit) {
		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0) {
			return;
		}

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
				&& (System.currentTimeMillis() < pEndzeit));
	}

	/**
	 * Die Lage einiger Kurse wird verändert. Falls sich die Bewertung verschlechter, wird die Veränderung rückgängig
	 * gemacht.
	 * 
	 * @return TRUE, falls sich die Bewertung verbessert hat.
	 */
	private boolean verteileKurse() {
		// Verändere die Kurslage 1-* mal ...
		do {
			// Ein Kurs wandert in eine andere Schiene.
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien();

			// Zustand-K besser?
			// Die Bewertung von Nichtwahlen und Kursdifferenzen ändert sich nicht, da keine
			// SuS verteilt werden. Aber die Bewertung der Fachwahlen FW = Fachart-Paar kann kleiner werden.
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return true;
			}
		} while (_random.nextBoolean());

		// Zustand laden.
		dynDaten.aktionZustandLadenK();
		return false;
	}

}
