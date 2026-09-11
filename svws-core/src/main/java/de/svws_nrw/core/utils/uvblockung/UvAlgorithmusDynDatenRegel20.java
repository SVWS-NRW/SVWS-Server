package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel20 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenFach fach;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int minimum;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param fach         Das {@link UvAlgorithmusDynDatenFach}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param minimum      Die minimale Anzahl an Lerngruppen mit dem Fach, das die Lehrkraft unterrichten soll.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel20(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenFach fach,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int minimum,
			final int prioritaet) {

		this.malus = malus;
		this.fach = fach;
		this.lehrkraft = lehrkraft;
		this.minimum = minimum;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		final int unterschreitung = minimum - lehrkraft.istFachZuLerngruppenAnzahl[fach.interneID];

		if ((prioritaet >= 0) && (unterschreitung > 0)) {
			malus[prioritaet] += faktor * unterschreitung;
		}
	}

}
