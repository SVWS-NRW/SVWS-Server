package de.svws_nrw.core.utils.uvblockung;

import java.util.List;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel 42 {@link UvBlockungRegelTyp#LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel42 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int min;
	private final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus              Das globale Malus-Array.
	 * @param lehrkraft          Die betroffene {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param min                Die minimale Anzahl an Lerngruppen in {@code lerngruppenMenge}, in denen die Lehrkraft vertreten sein soll.
	 * @param lerngruppenMenge   Die Menge der {@link UvAlgorithmusDynDatenLerngruppe}, auf die sich die Regel bezieht.
	 * @param prioritaet         Die Priorität, in deren Bucket der Malus eingetragen wird.
	 */
	public UvAlgorithmusDynDatenRegel42(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int min,
			final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge,
			final int prioritaet) {

		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.min = min;
		this.lerngruppenMenge = lerngruppenMenge;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		int vertreten = 0;
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppenMenge) {
			if (lerngruppe.gibIstLehrkraftZugeordnet(lehrkraft)) {
				vertreten++;
			}
		}

		final int unterschreitung = min - vertreten;
		if ((prioritaet >= 0) && (unterschreitung > 0)) {
			malus[prioritaet] += faktor * unterschreitung;
		}
	}

}
