package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel27 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenKlasse klasse;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft1;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft2;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param klasse       Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft1   Die 1. {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft2   Die 2. {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel27(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft1,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft2,
			final int prioritaet) {

		this.malus = malus;
		this.klasse = klasse;
		this.lehrkraft1 = lehrkraft1;
		this.lehrkraft2 = lehrkraft2;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		final int klasseID = klasse.interneID;

		// Beide Lehrkräfte müssen Stunden in dieser Klasse haben
		if ((prioritaet >= 0) && (lehrkraft1.istStundenProKlasse[klasseID]) > 0 && (lehrkraft2.istStundenProKlasse[klasseID] > 0)) {
			malus[prioritaet] += faktor;
		}
	}

}
