package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER}.
 * Sie wird auch von der Regel {@link UvBlockungRegelTyp#LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER} aufgerufen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel10 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int maxKlassenleitungen1;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param max1         Die maximale Anzahl an Klassenleitungen (Leitung 1).
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel10(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int max1,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.lehrkraft = lehrkraft;
		this.maxKlassenleitungen1 = max1;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel10: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		final int ueberschreitung = lehrkraft.istAnzahlLeitung1 - maxKlassenleitungen1;
		if (ueberschreitung > 0) {
			malus[prioritaet] += faktor * ueberschreitung;
		}
	}

}
