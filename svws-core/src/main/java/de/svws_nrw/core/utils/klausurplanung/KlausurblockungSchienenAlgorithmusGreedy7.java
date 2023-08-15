package de.svws_nrw.core.utils.klausurplanung;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/**
 * Die Strategie verteilt die Termine nacheinander.
 * Dabei werden immer versucht Klausuren zu nehmen, die bevorzugt in der gleichen Schiene sein sollen.
 *
 * @author Benjamin A. Bartsch
 */
public final class KlausurblockungSchienenAlgorithmusGreedy7 extends KlausurblockungSchienenAlgorithmusAbstract {

	/**
	 * Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public KlausurblockungSchienenAlgorithmusGreedy7(final @NotNull Random pRandom, final @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Termine auffüllen & Klausuren nach bevorzugter Lage";
	}

	@Override
	public void berechne(final long pZeitEnde) {
		_dynDaten.aktion_EntferneAlles_TermineNacheinander_KlausurenBevorzugterLage();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktion_EntferneAlles_TermineNacheinander_KlausurenBevorzugterLage();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
