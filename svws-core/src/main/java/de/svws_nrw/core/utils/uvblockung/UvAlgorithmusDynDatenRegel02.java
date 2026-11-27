package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#KLASSE_A_BENOETIGT_B_KLASSENLEHRER}.
 * Sie wird auch von der Regel {@link UvBlockungRegelTyp#KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER} aufgerufen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel02 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenKlasse klasse;
	private int sollLeitung1Anzahl;
	private int prioritaet;

	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log                  Der Logger für Debug-Zwecke.
	 * @param malus                Das globale Malus-Array.
	 * @param klasse               Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param sollLeitung1Anzahl   Die Anzahl an gewünschten Klassenleitungen (Leitung 1).
	 * @param prioritaet           Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel02(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final int sollLeitung1Anzahl,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.klasse = klasse;
		this.sollLeitung1Anzahl = sollLeitung1Anzahl;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	/**
	 * Setzt die neuen Werte für diese Regel und passt den Malus an.
	 *
	 * @param neueLeitung1Anzahl   Der neue Soll-Wert für die Anzahl an Klassenleitungen (Leitung 1).
	 * @param neuePrioritaet       Die neue Priorität für diese Regel.
	 */
	public void setzeLeitung1AnzahlUndPrioritaet(final int neueLeitung1Anzahl, final int neuePrioritaet) {
		if ((neuePrioritaet < 0) || (neuePrioritaet > UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel02: Ungültige Priorität %d - Änderung wird ignoriert.".formatted(neuePrioritaet));
			return;
		}
		changeMalus(-1);
		this.sollLeitung1Anzahl = neueLeitung1Anzahl;
		this.prioritaet = neuePrioritaet;
		changeMalus(1);
	}

	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel02: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		final int anzahlLeitung1 = klasse.leitung1Zugeordnet.size() + klasse.leitung1ZugeordnetFixiert.size();
		final int abweichung = Math.abs(sollLeitung1Anzahl - anzahlLeitung1);
		if (abweichung > 0) {
			malus[prioritaet] += faktor * abweichung;
		}
	}

}
