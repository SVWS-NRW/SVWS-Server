package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus arbeitet wie folgt:
 * <pre>
 * init: (1) Lade das derzeit beste Ergebnis.
 *
 * next: (1) Einige wenige Kurse werden verändert.
 *       (2) SuS mit "gewichteten bipartiten Matching" verteilen.
 *       (3) Verschlechterung ggf. rückgängig machen.
 *
 * </pre>
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusPermanentKOptimiereBest extends KursblockungAlgorithmusPermanentK {

	/**
	 * Im Konstruktor wird das derzeit beste Ergebnis geladen.
	 *
	 * @param random   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input    Die dynamischen Blockungsdaten.
	 * @param best     Der Zustand des derzeit besten Ergebnisses.
	 */
	public KursblockungAlgorithmusPermanentKOptimiereBest(final @NotNull Random random, final @NotNull Logger logger,
			final @NotNull GostBlockungsdatenManager input, final KursblockungDynDaten best) {
		super(random, logger, input);

		if (best == null) {
			// Erzeuge einen zufälligen Startzustand für Kurse und SuS.
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			dynDaten.aktionKurseFreieZufaelligVerteilen();
			dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
		} else {
			// Laden des bisher besten Ergebnisses.
			dynDaten.aktionZustandLadenVon(best);
		}

		dynDaten.aktionZustandSpeichernK();
	}

	@Override
	public @NotNull String toString() {
		return "KursblockungAlgorithmusPermanentKOptimiereBest";
	}

	@Override
	public void next(final long zeitEnde) {
		do {
			verteileKurse();
		} while (System.currentTimeMillis() < zeitEnde);
	}

	private void verteileKurse() {
		// Verteile einige wenige Kurse neu (mindestens einer) und prüfe, ob das Ergebnis besser wurde.
		do {
			dynDaten.aktionSchuelerAusAllenKursenEntfernen(); // Vor Kursverteilung müssen SuS entfernt sein.
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien(); // Verteile einen zufälligen Kurs neu.

			// Schülerverteilungsstrategie 1
			dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
			if (dynDaten.gibCompareZustandK1NW2KD3FW() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return; // Speichern und aufhören, da besser.
			}

			// Schülerverteilungsstrategie 2
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			dynDaten.aktionSchuelerVerteilenMitBipartitemMatching();
			if (dynDaten.gibCompareZustandK1NW2KD3FW() > 0) {
				dynDaten.aktionZustandSpeichernK();
				return; // Speichern und aufhören, da besser.
			}
		} while (rnd.nextBoolean());

		// Verschlechterung rückgängig machen.
		dynDaten.aktionZustandLadenK();
	}

	@Override
	public void ladeBestMitSchuelerverteilung() {
		dynDaten.aktionZustandLadenK();
	}

}
