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

	/** Mit diesem Algorithmus werden die SuS verteilt. */
	private final @NotNull KursblockungAlgorithmusSSchnellW algoS;

	/**
	 * Im Konstruktor wird das derzeit beste Ergebnis geladen.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input   Die dynamischen Blockungsdaten.
	 * @param best    Der Zustand des derzeit besten Ergebnisses.
	 */
	public KursblockungAlgorithmusPermanentKOptimiereBest(final @NotNull Random random, final @NotNull Logger logger, final @NotNull GostBlockungsdatenManager input, final KursblockungDynDaten best) {
		super(random, logger, input);
		algoS = new KursblockungAlgorithmusSSchnellW(random, logger, super.gibDynDaten());

		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0)
			return;

		if (best == null) {
			// Erzeuge einen zufälligen Startzustand für Kurse und SuS.
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			dynDaten.aktionKurseFreieZufaelligVerteilen();
			dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
			dynDaten.aktionZustandSpeichernK();
		} else {
			// Laden des bisher besten Ergebnisses, falls es nicht das selbe Objekt ist.
			dynDaten.aktionZustandLadenVon(best);
			dynDaten.aktionZustandSpeichernK();
		}
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
