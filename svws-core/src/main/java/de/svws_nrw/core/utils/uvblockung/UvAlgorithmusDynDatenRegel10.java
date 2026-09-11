package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER},
 * wird auch aufgerufen von Regel {@link UvBlockungRegelTyp#LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER},
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel10 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final @NotNull int maxKlassenleitungen1;
	private final @NotNull int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param max1         Die maximale Anzahl an Klassenleitungen (Leitung 1).
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel10(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int max1,
			final int prioritaet) {

		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.maxKlassenleitungen1 = max1;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		final int ueberschreitung = lehrkraft.istAnzahlLeitung1 - maxKlassenleitungen1;
		if ((prioritaet >= 0) && (ueberschreitung > 0)) {
			malus[prioritaet] += faktor * ueberschreitung;
		}
	}

}
