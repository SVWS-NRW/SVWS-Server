package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;

import de.nrw.schule.svws.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Algorithmus des <b>Typs K</b> verteilt die Kurse zunächst zufällig auf ihre Schienen. Dann wird die Lage durch
 * gezieltes Tauschen versucht zu verbessern.
 * 
 * @author Benjamin A. Bartsch
 */
public class KursblockungAlgorithmusKSchnellW extends KursblockungAlgorithmusK {

	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDat Die dynamischen Blockungsdaten.
	 */
	public KursblockungAlgorithmusKSchnellW(final @NotNull Random pRandom, final @NotNull Logger pLogger,
			final @NotNull KursblockungDynDaten pDynDat) {
		super(pRandom, pLogger, pDynDat);
	}
	
	@Override
	public @NotNull String toString() {
		return "SchnellW";
	}

	@Override
	public void berechne(final long pEndzeit) {
		// Keine Kursverteilung, wenn es keine freien Kurse gibt.
		if (dynDaten.gibKurseDieFreiSindAnzahl() == 0) {
			return;
		}

		// Entferne SuS aus den Kursen.
		dynDaten.aktionSchuelerAusAllenKursenEntfernen();

		// Verteile die Kurse beim ersten Start zufällig.
		dynDaten.aktionKurseFreieZufaelligVerteilen();

		// Verteile SuS zufällig.
		dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();

		// Speicherung des Start-Zustandes.
		dynDaten.aktionZustandSpeichernK();

		// Optimiere die Kurse. Bricht ab, wenn die Zeit vorbei ist.
		do {
			veraendereDieKurslageZufaelligEinWenig();
		} while (System.currentTimeMillis() < pEndzeit);

	}

	/**
	 * Kurslage wird ein wenig zufällig verändert und bewertet. Falls sich die Bewertung verschlechter, wird die
	 * Veränderung rückgängig gemacht.
	 */
	private void veraendereDieKurslageZufaelligEinWenig() {
		// Ein 1-* Kurse wandern zufällig in eine andere Schiene.
		do {
			// Entferne SuS, sonst dürfen Kurse nicht die Schiene wechseln.
			dynDaten.aktionSchuelerAusAllenKursenEntfernen();

			// Einen Kurs zufällig verteilen.
			dynDaten.aktionKursVerteilenEinenZufaelligenFreien();

			// SuS verteilen
			dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();

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
