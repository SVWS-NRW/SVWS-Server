package de.svws_nrw.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** 
 * Die Strategie 'Greedy1' verteilt die Klausuren in zufälliger Reihenfolge auf bereits 
 * existierende Schienen in zufälliger Reihenfolge. Ist dies nicht möglich, wird eine neue Schiene erzeugt.
 * 
 * @author Benjamin A. Bartsch */
public class KlausurblockungSchienenAlgorithmusGreedy1 extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten. 
	 */
	public KlausurblockungSchienenAlgorithmusGreedy1(final @NotNull Random pRandom, final @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Klausuren zufällig & Schienen zufällig";
	}

	@Override
	public void berechne(final long pZeitEnde) {
		_dynDaten.aktion_EntferneAlles_KlausurenZufaellig_SchienenZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktion_EntferneAlles_KlausurenZufaellig_SchienenZufaellig();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
