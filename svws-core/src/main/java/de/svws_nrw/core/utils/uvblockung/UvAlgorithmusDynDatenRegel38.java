package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel38 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenKlasse klasse;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus          Das globale Malus-Array.
	 * @param klasse         Die {@link UvAlgorithmusDynDatenKlasse};
	 * @param lehrkraft      Die {@link UvAlgorithmusDynDatenLehrkraft};
	 * @param prioritaet     Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel38(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int prioritaet) {

		this.malus = malus;
		this.klasse = klasse;
		this.lehrkraft = lehrkraft;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		final boolean istLeitung1 = lehrkraft.istLeitung1InKlasse[klasse.interneID];

		if ((prioritaet >= 0) && (istLeitung1)) {
			malus[prioritaet] += faktor;
		}
	}

}
