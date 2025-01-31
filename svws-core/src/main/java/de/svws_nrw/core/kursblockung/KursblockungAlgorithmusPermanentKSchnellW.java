package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus arbeitet wie folgt:
 * <pre>
 * init: (1) Alle Kurse zufällig verteilen
 *       (2) SuS mit "gewichteten bipartiten Matching" verteilen.
 *
 * next: (1) Einige wenige Kurse werden verändert.
 *       (2) SuS mit "gewichteten bipartiten Matching" verteilen.
 *       (3) Verschlechterung ggf. rückgängig machen.
 *
 * </pre>
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusPermanentKSchnellW extends KursblockungAlgorithmusPermanentK {

	/**
	 * Im Konstruktor wird ein zufälliger Anfangszustand erzeugt.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input   Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusPermanentKSchnellW(final @NotNull Random random, final @NotNull Logger logger,
			final @NotNull GostBlockungsdatenManager input) {
		super(random, logger, input);

		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0)
			return;

		// Erzeuge einen zufälligen Startzustand für Kurse und SuS.
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		dynDaten.aktionKurseFreieZufaelligVerteilen();
		dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
		dynDaten.aktionZustandSpeichernK();
	}

	@Override
	public @NotNull String toString() {
		return "KursblockungAlgorithmusPermanentKSchnellW";
	}

	@Override
	public void next(final long zeitEnde) {
		do {
			verteileKurseMitMatchingW();
		} while (System.currentTimeMillis() < zeitEnde);
	}

	private void verteileKurseMitMatchingW() {
		// Verteile einige wenige Kurse neu (mindestens einer) und prüfe, ob das Ergebnis besser wurde.
		do {
			// Entferne SuS, sonst dürfen Kurse nicht die Schiene wechseln.
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();

			// Einen Kurs zufällig verteilen.
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien();

			// Verteile SuS zufällig.
			dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();

			// Besser? --> Speichern.
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return;
			}
		} while (_random.nextBoolean());

		// Verschlechterung rückgängig machen.
		dynDaten.aktionZustandLadenK();
	}

}
