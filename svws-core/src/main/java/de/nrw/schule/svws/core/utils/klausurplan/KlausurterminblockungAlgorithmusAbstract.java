package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** 
 * Ein Algorithmus der diese Klasse erweitert verteilt Klausuren auf Termine (ohne zugeordnetem Datum). <br> 
 * Ziel ist es die minimale Anzahl an Terminen zu verwenden und dabei ggf. Regeln einzuhalten. <br>  
 * Pro Termin dürfen nicht zwei Klausuren landen, die dem selben Schüler zugeordnet sind.
 * 
 * @author Benjamin A. Bartsch 
 */
public abstract class KlausurterminblockungAlgorithmusAbstract {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	protected final @NotNull Random _random;

	/** Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges. */
	protected final @NotNull KlausurterminblockungDynDaten _dynDaten;

	/** Der Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public KlausurterminblockungAlgorithmusAbstract(@NotNull Random pRandom, @NotNull KlausurterminblockungDynDaten pDynDaten) {
		_random = pRandom;
		_dynDaten = pDynDaten;
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Klausuren auf Termine,
	 * beachtet dabei potentielle Regeln und überschreitet nicht die Endzeit (in Millisekunden).
	 * 
	 * @param pZeitEnde Die Endzeit (in Millisekunden). 
	 */
	public abstract void berechne(long pZeitEnde);

}
