package de.svws_nrw.core.utils.uvblockung;

import java.util.List;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel 43 {@link UvBlockungRegelTyp#LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel43 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int max;
	private final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge;
	private final int prioritaet;

	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus              Das globale Malus-Array.
	 * @param lehrkraft          Die betroffene {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param max                Die maximale Anzahl an Lerngruppen in {@code lerngruppenMenge}, in denen die Lehrkraft vertreten sein soll.
	 * @param lerngruppenMenge   Die Menge der {@link UvAlgorithmusDynDatenLerngruppe}, auf die sich die Regel bezieht.
	 * @param prioritaet         Die Priorität, in deren Bucket der Malus eingetragen wird.
	 */
	public UvAlgorithmusDynDatenRegel43(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int max,
			final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge,
			final int prioritaet) {

		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.max = max;
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

		final int ueberschreitung = vertreten - max;
		if ((prioritaet >= 0) && (ueberschreitung > 0)) {
			malus[prioritaet] += faktor * ueberschreitung;
		}
	}


}
