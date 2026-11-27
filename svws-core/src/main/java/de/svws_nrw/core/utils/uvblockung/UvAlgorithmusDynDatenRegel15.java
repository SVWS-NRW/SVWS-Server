package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel15 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel15(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.lehrkraft = lehrkraft;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel15: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		if (!lerngruppe.gibIstLehrkraftZugeordnet(lehrkraft)) {
			malus[prioritaet] += faktor;
		}
	}

}
