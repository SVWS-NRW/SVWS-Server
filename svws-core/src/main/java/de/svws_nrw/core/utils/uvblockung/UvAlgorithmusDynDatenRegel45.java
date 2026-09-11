package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B},
 * wird auch aufgerufen von Regel {@link UvBlockungRegelTyp#LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A},
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel45 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private boolean aktiviert;
	private int prioritaet;

	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die betroffene {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param aktiviert    TRUE, falls die Soll-Ist-Abweichung der Lehrkraft aktiv sein soll.
	 * @param prioritaet   Die Priorität, in deren Bucket der Malus eingetragen wird.
	 */
	public UvAlgorithmusDynDatenRegel45(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final boolean aktiviert,
			final int prioritaet) {

		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.aktiviert = aktiviert;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	/**
	 * Aktualisiert diese Regel.
	 *
	 * @param aktiviert    Die neue Aktivierung.
	 * @param prioritaet   Die neue Priorität.
	 */
	void setzeAuf(final boolean aktiviert, final int prioritaet) {
		this.changeMalus(-1);
		this.aktiviert = aktiviert;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if (!aktiviert) {
			return;
		}

		final double abweichung = lehrkraft.stundenSoll - lehrkraft.stundenAnrechnung - lehrkraft.istStundensummeLerngruppen;
		final int gerundet = (int) (abweichung * abweichung);

		if ((prioritaet >= 0) && (gerundet > 0)) {
			malus[prioritaet] += faktor * gerundet;
		}
	}


}
