package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel40 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int maximum;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param maximum      Die maximale Anzahl an Lerngruppen.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel40(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int maximum,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.maximum = maximum;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel40: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		final int ueberschreitung = lehrkraft.istAnzahlLerngruppen - maximum;

		if (ueberschreitung > 0) {
			malus[prioritaet] += faktor * ueberschreitung;
		}
	}

}
