package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;

import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Kursverteilungs-Algorithmus verteilt die Kurse auf ihre Schienen. Die SuS werden nacheinander mit einem
 * angepassten Matching-Algorithmus auf die Kurse verteilt. Das besondere dabei ist, dass ein S. beim Matching sich auch
 * wünschen kann, dass ein Kurs die Schiene wechselt.
 * 
 * @author Benjamin A. Bartsch
 */
public class KursblockungAlgorithmusKSchuelervorschlag extends KursblockungAlgorithmusK {

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
	public KursblockungAlgorithmusKSchuelervorschlag(@NotNull Random pRandom, @NotNull Logger pLogger,
			@NotNull KursblockungDynDaten pDynDat) {
		super(pRandom, pLogger, pDynDat);
	}

	@Override
	public @NotNull String toString() {
		return "Schülervorschlag";
	}

	/**
	 * Der Algorithmus entfernt zunächst alle SuS aus ihren Kursen. Anschließend werden die Kurse zufällig verteilt.
	 * Anschließend verändert der Algorithmus die Lage eines zufälligen Kurses. Falls sich die Bewertung verschlechter,
	 * wird die Veränderung rückgängig gemacht.
	 */
	@Override
	public void berechne(long pEndzeit) {
		long current = System.currentTimeMillis();
		long halbzeit = current + (pEndzeit - current) / 2;

		// Keine Kurverteilung, wenn es keine freien Kurse gibt.
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
			countKeineVerbesserung = verteileKurseMitSchuelerwunsch() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG)
				&& (System.currentTimeMillis() < halbzeit));

		countKeineVerbesserung = 0;
		do {
			countKeineVerbesserung = verteileKurseZufaelligEinWenig() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG)
				&& (System.currentTimeMillis() < pEndzeit));

	}

	/**
	 * Kurslage wird durch Schüler-Wünsche verändert. Falls sich die Bewertung verschlechter, wird die Veränderung
	 * rückgängig gemacht.
	 */
	private boolean verteileKurseMitSchuelerwunsch() {

		// Entferne SuS, sonst dürfen Kurse nicht die Schiene wechseln.
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();

		// Verteile die Kurse nach dem Wunsch der SuS.
		dynDaten.aktionKurseVerteilenNachSchuelerwunsch();

		// SuS verteilen
		dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();

		// Besser oder gleich? --> Speichern.
		int compare = dynDaten.gibCompareZustandK_NW_KD_FW();
		if (compare >= 0) {
			dynDaten.aktionZustandSpeichernK();
			return compare > 0; // besser?
		}

		// Schlechter
		dynDaten.aktionZustandLadenK();
		return false;
	}

	/**
	 * Kurslage wird ein wenig zufällig verändert und bewertet. Falls sich die Bewertung verschlechter, wird die
	 * Veränderung rückgängig gemacht.
	 */
	private boolean verteileKurseZufaelligEinWenig() {
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
