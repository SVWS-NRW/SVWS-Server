package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE}.
 * Sie wird auch von der Regel {@link UvBlockungRegelTyp#LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE} aufgerufen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel06 implements UvAlgorithmusDynDatenRegel {


	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenKlasse klasse;
	private final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft;
	private final int minKlassenstundenBeiLeitung1;
	private final int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log          Der Logger für Debug-Zwecke.
	 * @param malus        Das globale Malus-Array.
	 * @param klasse       Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param min          Die minimale Anzahl an Klassenstunden bei Klassenleitung (Leitung 1).
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel06(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int min,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.klasse = klasse;
		this.lehrkraft = lehrkraft;
		this.minKlassenstundenBeiLeitung1 = min;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel06: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		final double differenz = minKlassenstundenBeiLeitung1 - lehrkraft.istStundenProKlasse[klasse.interneID];
		final int fehlendeStunden = (int) Math.ceil(differenz - 0.5);
		if ((lehrkraft.istLeitung1InKlasse[klasse.interneID]) && (fehlendeStunden > 0)) {
			malus[prioritaet] += faktor * fehlendeStunden;
		}
	}

}
