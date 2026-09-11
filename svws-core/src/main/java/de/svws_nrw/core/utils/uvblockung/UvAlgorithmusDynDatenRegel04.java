package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER},
 * wird auch aufgerufen von Regel {@link UvBlockungRegelTyp#KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER},
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel04 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenKlasse klasse;
	private @NotNull int sollLeitung2Anzahl;
	private @NotNull int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus                Das globale Malus-Array.
	 * @param klasse               Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param sollLeitung2Anzahl   Die Anzahl an gewünschten Klassenleitungen (Leitung 2).
	 * @param prioritaet           Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel04(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final int sollLeitung2Anzahl,
			final int prioritaet) {

		this.malus = malus;
		this.klasse = klasse;
		this.sollLeitung2Anzahl = sollLeitung2Anzahl;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	/**
	 * Setzt die neuen Werte für diese Regel und passt den Malus an.
	 *
	 * @param neueLeitung2Anzahl   Der neue Soll-Wert für die Anzahl an stellv. Klassenleitungen (Leitung 2).
	 * @param neuePrioritaet       Die neue Priorität für diese Regel.
	 */
	public void setzeLeitung2AnzahlUndPrioritaet(final int neueLeitung2Anzahl, final int neuePrioritaet) {
		changeMalus(-1);
		this.sollLeitung2Anzahl = neueLeitung2Anzahl;
		this.prioritaet = neuePrioritaet;
		changeMalus(1);
	}

	@Override
	public void changeMalus(final int faktor) {
		final int anzahlLeitung2 = klasse.leitung2Zugeordnet.size() + klasse.leitung2ZugeordnetFixiert.size();
		final int abweichung = Math.abs(sollLeitung2Anzahl - anzahlLeitung2);
		if ((prioritaet >= 0) && (abweichung > 0)) {
			malus[prioritaet] += faktor * abweichung;
		}
	}

}
