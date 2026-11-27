package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel41 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int minimum;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param minimum      Die minimale Anzahl an Lerngruppen.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel41(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int minimum,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.minimum = minimum;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel41: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		final int unterschreitung = minimum - lehrkraft.istAnzahlLerngruppen;

		if (unterschreitung > 0) {
			malus[prioritaet] += faktor * unterschreitung;
		}
	}

}
