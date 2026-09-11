package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel15 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final @NotNull int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel15(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int prioritaet) {

		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.lehrkraft = lehrkraft;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet >= 0) && !lerngruppe.gibIstLehrkraftZugeordnet(lehrkraft)) {
			malus[prioritaet] += faktor;
		}
	}

}
