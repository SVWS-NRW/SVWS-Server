package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus arbeitet wie folgt:
 * <br> Die Kurse werden Fachgruppenweise verändert.
 * <br> Die SuS werden mit "gewichteten bipartiten Matching" verteilt.
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusPermanentKFachgruppe extends KursblockungAlgorithmusPermanentK {

	/**
	 * Im Konstruktor wird ein zufälliger Anfangszustand erzeugt.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input   Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusPermanentKFachgruppe(final @NotNull Random random, final @NotNull Logger logger,
			final @NotNull GostBlockungsdatenManager input) {
		super(random, logger, input);

		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0) {
			return;
		}

		// Erzeuge einen zufälligen Startzustand-K für Kurse und SuS.
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		dynDaten.aktionKurseFreieZufaelligVerteilen();
		dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
		dynDaten.aktionZustandSpeichernK();
	}

	@Override
	public @NotNull String toString() {
		return "KursblockungAlgorithmusPermanentKFachgruppe";
	}

	@Override
	public void next(final long zeitEnde) {
		do {
			verteileKurse();
		} while (System.currentTimeMillis() < zeitEnde);
	}

	/**
	 * Kurse werden so verteilt, dass immer eine Fachgruppe zerstört und neuverteilt wird.
	 */
	private void verteileKurse() {
		do {
			dynDaten.aktionSchuelerAusAllenKursenEntfernen(); // Vor Kursverteilung müssen SuS entfernt sein.
			dynDaten.aktionKursVerteilenEineZufaelligeFachgruppe(); // Verteile eine Fachgruppe neu.

			// Schülerverteilungsstrategie 1
			dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return; // Speichern und aufhören, da besser.
			}

			// Schülerverteilungsstrategie 2
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			dynDaten.aktionSchuelerVerteilenMitBipartitemMatching();
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return; // Speichern und aufhören, da besser.
			}

		} while (_random.nextBoolean());

		// Verschlechterung rückgängig machen.
		dynDaten.aktionZustandLadenK();
	}

	@Override
	public void ladeBestMitSchuelerverteilung() {
		dynDaten.aktionZustandLadenK();
	}

}
