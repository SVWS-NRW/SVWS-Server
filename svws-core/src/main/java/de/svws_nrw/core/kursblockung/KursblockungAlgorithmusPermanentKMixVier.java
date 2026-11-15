package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * KursblockungAlgorithmusPermanentKMixVier:
 * <br> 40 % Zufallskurs,
 * <br> 10 % Fachgruppe,
 * <br> 20 % Tausch(12),
 * <br> 30 % Tausch(1).
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusPermanentKMixVier extends KursblockungAlgorithmusPermanentK {

	/** Zweck: Damit die Initialisierung nur einmal ausgeführt wird. */
	private boolean initialisiert = false;

	/**
	 * Erzeugt den Algorithmus.
	 *
	 * @param random   das Zufallsobjekt
	 * @param logger   der Logger
	 * @param input    die Eingabedaten
	 */
	public KursblockungAlgorithmusPermanentKMixVier(
			final @NotNull Random random,
			final @NotNull Logger logger,
			final @NotNull GostBlockungsdatenManager input) {
		super(random, logger, input);
	}

	@Override
	public void next(final long zeitEnde) {
		// Initialisierung erfolgt über ein boolean,
		// damit diese bei Benchmarks mit zu Gesamt-Rechenzeit gezählt wird.
		if (!initialisiert) {
			init();
			initialisiert = true;
		}
		// Iteriere und speichere, wenn ein besseres Zwischenergebnis gefunden wurde.
		while (System.currentTimeMillis() < zeitEnde) {
			if (schritt()) {
				dynDaten.aktionZustandSpeichernK();
			} else {
				dynDaten.aktionZustandLadenK();
			}
		}
	}

	@Override
	public void ladeBestMitSchuelerverteilung() {
		// Lade das beste Zwischenergebnis.
		dynDaten.aktionZustandLadenK();
	}

	private void init() {
		dynDaten.aktionStartzustandWunschkaskade();
	}

	private boolean schritt() {
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();

		// Kurse verteilen
		zuege();

		// Kurs-Reparaturen
		dynDaten.aktionKurseVerteilenNachSchuelerwunsch();
		for (int i = 0; (i < 8) && dynDaten.aktionKurseVerteilenNachSchuelerwunschSingle(); i++) {
			// nichts
		}

		// Schüler verteilen
		dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
		dynDaten.aktionSchuelerVerteilenMitBipartitemMatching();
		return dynDaten.gibCompareZustandK1NW2KD3FW() >= 0;
	}

	private void zuege() {
		final int r = rnd.nextInt(100);
		if (r < 40) {
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
		} else if (r < 50) {
			dynDaten.aktionKursVerteilenEineZufaelligeFachgruppe();
		} else if (r < 70) {
			dynDaten.aktionKurseTauschen(12);
		} else {
			dynDaten.aktionKurseTauschen(1);
		}
	}

}
