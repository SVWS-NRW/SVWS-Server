package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER},
 * wird auch aufgerufen von Regel {@link UvBlockungRegelTyp#LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER},
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel12 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final @NotNull int maxKlassenleitungen2;
	private final @NotNull int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param max2         Die maximale Anzahl an Klassenleitungen (Leitung 2).
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel12(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int max2,
			final int prioritaet) {

		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.maxKlassenleitungen2 = max2;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		final int ueberschreitung = lehrkraft.istAnzahlLeitung2 - maxKlassenleitungen2;
		if ((prioritaet >= 0) && (ueberschreitung > 0)) {
			malus[prioritaet] += faktor * ueberschreitung;
		}
	}

}
