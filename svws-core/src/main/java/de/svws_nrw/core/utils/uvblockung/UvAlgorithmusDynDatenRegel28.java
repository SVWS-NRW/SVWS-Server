package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel28 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe;
	private int sollAnzahlLehrkraefte;
	private int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus                   Das globale Malus-Array.
	 * @param lerngruppe              Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param sollAnzahlLehrkraefte   Die Anzahl an Lehrkräften die diese Lerngruppe benötigt.
	 * @param prioritaet              Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel28(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final int sollAnzahlLehrkraefte,
			final int prioritaet) {

		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.sollAnzahlLehrkraefte = sollAnzahlLehrkraefte;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}

	void setzeAuf(final int neuesSoll, final int prioritaet) {
		this.changeMalus(-1);
		this.sollAnzahlLehrkraefte = neuesSoll;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		final int anzahl = lerngruppe.lehrkraefteZugeordnetAlle.size();
		final int abweichung = Math.abs(sollAnzahlLehrkraefte - anzahl);

		if ((prioritaet >= 0) && (abweichung > 0)) {
			malus[prioritaet] += faktor * abweichung;
		}
	}

}
