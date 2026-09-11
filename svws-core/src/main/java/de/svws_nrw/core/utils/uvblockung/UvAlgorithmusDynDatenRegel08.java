package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE},
 * wird auch aufgerufen von Regel {@link UvBlockungRegelTyp#LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE},
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel08 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenKlasse klasse;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final @NotNull int minKlassenstundenBeiLeitung2;
	private final @NotNull int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param klasse       Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param min          Die minimale Anzahl an Klassenstunden bei Klassenleitung (Leitung 2).
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel08(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int min,
			final int prioritaet) {

		this.malus = malus;
		this.klasse = klasse;
		this.lehrkraft = lehrkraft;
		this.minKlassenstundenBeiLeitung2 = min;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		final double differenz = minKlassenstundenBeiLeitung2 - lehrkraft.istStundenProKlasse[klasse.interneID];
		if ((lehrkraft.istLeitung2InKlasse[klasse.interneID]) && (differenz > 0)) {
			malus[prioritaet] += faktor * differenz;
		}
	}

}
