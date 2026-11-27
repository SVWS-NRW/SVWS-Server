package de.svws_nrw.core.utils.uvblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * RW1: <b>3x Ring2 + Kette4 (Ring zuerst) x4</b>: Vier Runden: drei 2er-Tausche, dann vier Ketten.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusImplRandomWalk1 extends UvAlgorithmusImplRandomWalk {

	/**
	 * Der Konstruktor.
	 *
	 * @param log   Ein {@link Logger}-Objekt für Debug-Zwecke.
	 * @param rnd   Ein {@link Random}-Objekt zur Steuerung des Zufalls.
	 * @param dyn   Die dynamischen Daten (mit dynamischer Bewertung).
	 */
	public UvAlgorithmusImplRandomWalk1(final @NotNull Logger log, final @NotNull Random rnd, final @NotNull UvAlgorithmusDynDaten dyn) {
		super(log, rnd, dyn);
	}


	@Override
	protected void individuelleStrategie() {
		dyn.strategieMixRingZuerst(4, 3, 0, 4, 2);
	}

}
