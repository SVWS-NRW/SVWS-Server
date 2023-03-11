package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** Die Strategie verteilt wie die Strategie {@link KlausurblockungSchienenAlgorithmusGreedy1}, nur werden bei dieser
 * Strategie die Schienen nacheinander aufgefüllt.
 * 
 * @author Benjamin A. Bartsch */
public class KlausurblockungSchienenAlgorithmusGreedy1b extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten. 
	 */
	public KlausurblockungSchienenAlgorithmusGreedy1b(@NotNull Random pRandom, @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Klausuren zufällig & Schienen nacheinander";
	}

	@Override
	public void berechne(long pZeitEnde) {
		_dynDaten.aktionEntferneAllesFuelleSchienenNacheinandeAuf();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktionEntferneAllesFuelleSchienenNacheinandeAuf();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
