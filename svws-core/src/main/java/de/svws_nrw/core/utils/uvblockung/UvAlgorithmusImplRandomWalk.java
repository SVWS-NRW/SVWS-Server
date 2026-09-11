package de.svws_nrw.core.utils.uvblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wendet zwei Manipulationen an:
 * <br> optimiere1: Eine Lerngruppe bekommt eine zufällige Lehrkraft.
 * <br> optimiere2: Eine Lerngruppe entfernt eine zufällige Lehrkraft.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusImplRandomWalk extends UvAlgorithmusImplAbstract {


	/**
	 * Der Konstruktor.
	 *
	 * @param log   Ein {@link Logger}-Objekt für Debug-Zwecke.
	 * @param rnd   Ein {@link Random}-Objekt zur Steuerung des Zufalls.
	 * @param dyn   Die dynamischen Daten (mit dynamischer Bewertung).
	 */
	public UvAlgorithmusImplRandomWalk(final @NotNull Logger log, final @NotNull Random rnd, final @NotNull UvAlgorithmusDynDaten dyn) {
		super(log, rnd, dyn);
	}


	@Override
	public void berechneInnerhalb(final long zeitlimit) {
		final long zeitEnde = System.currentTimeMillis() + zeitlimit;

		while (System.currentTimeMillis() < zeitEnde) {
			dyn.strategieLerngruppeLehrkraftHinzufuegen();
			dyn.strategieLerngruppeLehrkraftEntfernen();
			dyn.strategieKlassenleitung1Hinzufuegen();
			dyn.strategieKlassenleitung1Entfernen();
			dyn.strategieKlassenleitung2Hinzufuegen();
			dyn.strategieKlassenleitung2Entfernen();
		}

	}

}
