package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER}.
 * Sie wird auch von der Regel {@link UvBlockungRegelTyp#LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER} aufgerufen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel12 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int maxKlassenleitungen2;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param max2         Die maximale Anzahl an stellv. Klassenleitungen (Leitung 2).
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel12(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int max2,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.maxKlassenleitungen2 = max2;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel12: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		final int ueberschreitung = lehrkraft.istAnzahlLeitung2 - maxKlassenleitungen2;
		if (ueberschreitung > 0) {
			malus[prioritaet] += faktor * ueberschreitung;
		}
	}

}
