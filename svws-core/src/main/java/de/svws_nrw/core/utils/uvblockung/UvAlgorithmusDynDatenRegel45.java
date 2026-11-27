package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B}.
 * Sie wird auch von der Regel {@link UvBlockungRegelTyp#LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A} aufgerufen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel45 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private boolean aktiviert;
	private int prioritaet;

	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die betroffene {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param aktiviert    TRUE, falls die Soll-Ist-Abweichung der Lehrkraft aktiv sein soll.
	 * @param prioritaet   Die Priorität, in deren Bucket der Malus eingetragen wird.
	 */
	public UvAlgorithmusDynDatenRegel45(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final boolean aktiviert,
			final int prioritaet) {

		this.log = log;
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
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel45: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		if (!aktiviert) {
			return;
		}

		final double abweichung = lehrkraft.stundenSoll - lehrkraft.stundenAnrechnung - lehrkraft.istStundensummeLerngruppen;
		final int gerundet = (int) Math.ceil((abweichung * abweichung) - 0.5);

		if (gerundet > 0) {
			malus[prioritaet] += faktor * gerundet;
		}
	}


}
