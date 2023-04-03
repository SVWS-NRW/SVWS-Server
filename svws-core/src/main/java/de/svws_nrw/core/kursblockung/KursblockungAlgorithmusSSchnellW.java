package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Schülerverteilungs-Algorithmus verteilt die SuS auf ihre Kurse. Die Parameter sind so eingestellt, dass der
 * Algorithmus relativ schnell ist und dennoch eine hohe Güte hat. Benchmarks zeigen, dass er ca. 10 - 30 Millisekunden
 * benötigt.
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusSSchnellW extends KursblockungAlgorithmusS {

	/**
	 * Array der SuS, deren Kurse verteilt werden sollen.
	 */
	private final @NotNull KursblockungDynSchueler @NotNull [] schuelerArr;

	/**
	 * Zur Speicherung einer zufälligen Permutation der Indizes der Schüler.
	 */
	private final @NotNull int[] perm;

	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in dieser Methode noch nicht
	 * auf Schienen verteilt werden.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger zum Protokollieren von Warnungen und Fehlern.
	 * @param pDynDat Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusSSchnellW(final @NotNull Random pRandom, final @NotNull Logger pLogger,
			final @NotNull KursblockungDynDaten pDynDat) {
		super(pRandom, pLogger, pDynDat);
		schuelerArr = pDynDat.gibSchuelerArrayAlle();
		perm = KursblockungStatic.gibPermutation(_random, schuelerArr.length);
	}

	/**
	 * Der Algorithmus verteilt die SuS auf ihre Kurse zufällig. Kommt es während des Verteilens zur Kollision, so wird
	 * der Kurs nicht gewählt.
	 */
	@Override
	public void berechne() {
		// Speicherung der Statistik (Zustand S).
		dynDaten.gibStatistik().aktionBewertungSpeichernS();

		// Optimiere die Verteilung der SuS 10 mal.
		for (int i = 0; i < 10; i++)
			verteileSchuelerAlle();
	}

	/**
	 * Der Algorithmus verteilt die SuS in zufälliger Reihenfolge ein weiteres Mal zufällig. Falls die Verteilung
	 * schlechter ist, wird der vorherige Zustand wiederhergestellt.
	 *
	 * @return TRUE, falls der Zustand sich verbessert hat.
	 */
	private boolean verteileSchuelerAlle() {
		boolean verbesserung = false;

		KursblockungStatic.aktionPermutiere(_random, perm);
		for (int p = 0; p < schuelerArr.length; p++) {
			final int i = perm[p];
			verbesserung |= verteileSchuelerEiner(schuelerArr[i]);
		}

		return verbesserung;
	}

	private boolean verteileSchuelerEiner(final @NotNull KursblockungDynSchueler schueler) {
		// Kurszuordnung des Schülers speichern.
		dynDaten.gibStatistik().aktionBewertungSpeichernS();
		schueler.aktionZustandSpeichernS();

		// S. auf seine Kurse neu verteilen.
		schueler.aktionKurseAlleEntfernen();
		schueler.aktionKurseVerteilenNurMultikurseZufaellig();
		schueler.aktionKurseVerteilenNurFachartenMitEinemKurs();
		schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtetem();

		// Schlechter? --> Kurszuordnung zurück.
		final int cmp = dynDaten.gibStatistik().gibBewertungZustandS_NW_KD();
		if (cmp < 0)
			schueler.aktionZustandLadenS();

		// Besser?
		return cmp > 0;
	}

}
