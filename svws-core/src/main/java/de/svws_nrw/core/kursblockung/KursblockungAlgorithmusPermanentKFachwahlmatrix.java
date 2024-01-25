package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus arbeitet wie folgt:
 * <br> init: ...
 * <br> next: ...
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

		// Entferne SuS aus den Kursen (vorsichtshalber wegen alter Berechnungen).
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();

		// Verteile die Kurse beim ersten Start zufällig.
		dynDaten.aktionKurseFreieZufaelligVerteilen();

		// Speicherung des Start-Zustandes.
		dynDaten.aktionZustandSpeichernK();

		// SuS werden bei diesem Algorithmus nicht verteilt.
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

	/**
	 * Die Lage einiger Kurse wird verändert. Falls sich die Bewertung verschlechter, wird die Veränderung rückgängig gemacht.
	 */
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

		dynDaten.aktionZustandLadenK();
	}

}
