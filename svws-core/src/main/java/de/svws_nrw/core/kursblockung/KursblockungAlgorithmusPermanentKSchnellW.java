package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus arbeitet wie folgt:
 * <br> init: Die Kurse werden zufällig verteilt, die SuS mit "gewichteten bipartiten Matching".
 * <br> next: Die SuS werden permanent mit "gewichteten bipartiten Matching"
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
	public KursblockungAlgorithmusPermanentKSchnellW(final @NotNull Random random, final @NotNull Logger logger, final @NotNull GostBlockungsdatenManager input) {
		super(random, logger, input);

		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0)
			return;

		// Entferne SuS aus den Kursen.
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();

		// Verteile die Kurse beim ersten Start zufällig.
		dynDaten.aktionKurseFreieZufaelligVerteilen();

		// Verteile SuS zufällig.
		dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();

		// Speicherung des Start-Zustandes.
		dynDaten.aktionZustandSpeichernK();
	}

	@Override
	public void next(final long zeitEnde) {
		do {
			optimierte();
		} while (System.currentTimeMillis() < zeitEnde);
	}

	/**
	 * Die Lage einiger Kurse wird verändert. Falls sich die Bewertung verschlechter, wird die Veränderung rückgängig gemacht.
	 */
	private void optimierte() {
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

		dynDaten.aktionZustandLadenK();
	}

}
