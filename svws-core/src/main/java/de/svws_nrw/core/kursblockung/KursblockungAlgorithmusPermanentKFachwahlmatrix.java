package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus arbeitet wie folgt:
 * <pre>
 * init: (1) Alle Kurse zufällig verteilen. SuS werden nicht verteilt.
 *
 * next: (1) Einige wenige Kurse werden verändert. SuS werden nicht verteilt.
 *       (2) Bei Verschlechterung der Fachwahl-Bewertung Veränderung rückgängig machen.
 *
 * </pre>
 *
 * <br> init: Die Kurse werden zufällig verteilt (ohne SuS).
 * <br> next: Die Kurse werden weiterhin zufällig verteilt (ohne SuS). Bewertet wird nur die Fachwahl-Lage der Kurse.
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusPermanentKFachwahlmatrix extends KursblockungAlgorithmusPermanentK {


	/**
	 * Im Konstruktor wird ein zufälliger Anfangszustand erzeugt.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input   Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusPermanentKFachwahlmatrix(final @NotNull Random random, final @NotNull Logger logger, final @NotNull GostBlockungsdatenManager input) {
		super(random, logger, input);

		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0)
			return;

		// Erzeuge einen zufälligen Startzustand für Kurse. SuS werden bei diesem Algorithmus nicht verteilt.
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		dynDaten.aktionKurseFreieZufaelligVerteilen();
		dynDaten.aktionZustandSpeichernK();
	}

	@Override
	public @NotNull String toString() {
		return "KursblockungAlgorithmusPermanentKFachwahlmatrix";
	}

	@Override
	public void next(final long zeitEnde) {
		do {
			optimiere();
		} while (System.currentTimeMillis() < zeitEnde);
	}

	private void optimiere() {
		// Verteile einige wenige Kurse neu (mindestens einer) und prüfe, ob das Ergebnis besser wurde.
		do {
			// Ein Kurs wandert in eine andere Schiene.
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien();

			// Besser? --> Speichern.
			// Die Bewertung von Nichtwahlen und Kursdifferenzen ändert sich nicht, da keine
			// SuS verteilt werden. Aber die Bewertung der Fachwahlen FW = Fachart-Paar kann kleiner werden.
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return;
			}
		} while (_random.nextBoolean());

		// Verschlechterung rückgängig machen.
		dynDaten.aktionZustandLadenK();
	}

}
