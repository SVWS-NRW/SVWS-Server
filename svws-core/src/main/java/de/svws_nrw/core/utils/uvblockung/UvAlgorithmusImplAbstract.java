package de.svws_nrw.core.utils.uvblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Algorithmus der diese Klasse erweitert dient dazu Lehrkräfte auf Lerngruppen zu verteilen.
 * <br>Ziel ist es:
 * <br>- TODO Regeln einzuhalten (später).
 * <br>- Eine Berechnung innerhalb eines Zeitlimits durchzuführen.
 * <br>- Eine Zuordnung von Lehrkräften auf Lerngruppen zu liefern.
 *
 * @author Benjamin A. Bartsch
 */
public abstract class UvAlgorithmusImplAbstract {

	/** Ein Logger für Debug-Zwecke. */
	private final @NotNull Logger log;

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	protected final @NotNull Random rnd;

	/** Die dynamischen Daten (mit dynamischer Bewertung). */
	protected final @NotNull UvAlgorithmusDynDaten dyn;

	/**
	 * Der Konstruktor.
	 *
	 * @param log   Ein {@link Logger}-Objekt für Debug-Zwecke.
	 * @param rnd   Ein {@link Random}-Objekt zur Steuerung des Zufalls.
	 * @param dyn   Die dynamische Daten (mit dynamischer Bewertung).
	 */
	protected UvAlgorithmusImplAbstract(final @NotNull Logger log, final @NotNull Random rnd, final @NotNull UvAlgorithmusDynDaten dyn) {
		this.log = log;
		this.rnd = rnd;
		this.dyn = dyn;
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Lehrkräfte auf die Lerngruppen und
	 * überschreitet dabei nicht das Zeitlimit (in Millisekunden).
	 *
	 * @param zeitlimit   Das Zeitlimit (in Millisekunden).
	 */
	public abstract void berechneInnerhalb(long zeitlimit);

}
