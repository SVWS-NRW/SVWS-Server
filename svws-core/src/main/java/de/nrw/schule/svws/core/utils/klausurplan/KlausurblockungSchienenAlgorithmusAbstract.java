package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.Random;

import de.nrw.schule.svws.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Ein Algorithmus der diese Klasse erweitert dient dazu Klausuren auf Schienen zu verteilen. Ziel ist es die minimale
 * Anzahl an Schienen zu verwenden. In einer Schiene dürfen nicht zwei Klausuren landen, die dem selben Schüler
 * zugeordnet sind.
 * 
 * @author Benjamin A. Bartsch */
public abstract class KlausurblockungSchienenAlgorithmusAbstract {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	protected final @NotNull Random _random;

	/** Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges. */
	protected final @NotNull KlausurblockungSchienenDynDaten _dynDaten;

	/** Der Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public KlausurblockungSchienenAlgorithmusAbstract(@NotNull Random pRandom, @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		_random = pRandom;
		_dynDaten = pDynDaten;
	}

	/** Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Klausuren auf die Schienen und
	 * überschreitet dabei nicht die Endzeit (in Millisekunden).
	 * 
	 * @param pZeitEnde Die Endzeit (in Millisekunden). */
	public abstract void berechne(long pZeitEnde);

}
