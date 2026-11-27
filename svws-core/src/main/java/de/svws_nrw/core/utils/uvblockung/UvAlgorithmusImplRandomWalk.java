package de.svws_nrw.core.utils.uvblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Ein RandomWalk-Algorithmus mit gemeinsamer Grundmethode: Jede Iteration probiert zunächst die sechs
 * Basis-Strategien und führt anschließend die individuelle Strategie der Unterklasse aus. Die
 * Unterklassen enthalten ausschließlich ihre einfache individuelle Strategie und keine eigenen Felder.
 *
 * @author Benjamin A. Bartsch
 */
public abstract class UvAlgorithmusImplRandomWalk extends UvAlgorithmusImplAbstract {

	/**
	 * Der Konstruktor.
	 *
	 * @param log   Ein {@link Logger}-Objekt für Debug-Zwecke.
	 * @param rnd   Ein {@link Random}-Objekt zur Steuerung des Zufalls.
	 * @param dyn   Die dynamischen Daten (mit dynamischer Bewertung).
	 */
	protected UvAlgorithmusImplRandomWalk(final @NotNull Logger log, final @NotNull Random rnd, final @NotNull UvAlgorithmusDynDaten dyn) {
		super(log, rnd, dyn);
	}


	/**
	 * Die individuelle Strategie der Unterklasse (einfach, ohne eigene Felder).
	 */
	protected abstract void individuelleStrategie();


	/**
	 * Die Grundmethode: probiert eine zufällige Basis-Strategie und führt danach die individuelle
	 * Strategie der Unterklasse aus.
	 */
	private void grundmethode() {
		dyn.basisStrategieZufaellig();
		individuelleStrategie();
	}


	/**
	 * Führt wiederholt die Grundmethode aus, bis das Zeitlimit erreicht ist.
	 *
	 * @param zeitlimit   Das Zeitlimit (in Millisekunden).
	 */
	@Override
	public void berechneInnerhalb(final long zeitlimit) {
		final long zeitEnde = System.currentTimeMillis() + zeitlimit;

		while (System.currentTimeMillis() < zeitEnde) {
			grundmethode();
		}

	}

}
